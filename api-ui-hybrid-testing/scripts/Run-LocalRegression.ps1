<#
.SYNOPSIS
Build the pinned official application, run isolated Java 17 regression, and stop only owned processes.
#>
[CmdletBinding()]
param(
    [string]$TestJavaHome,
    [string]$AppJavaHome,
    [ValidateSet('smoke','api','ui','integration','regression')][string]$Groups = 'regression',
    [switch]$Headed,
    [switch]$SkipBuild
)
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$toolsRoot = Join-Path $projectRoot '.tools'
$revision = 'efe2deb8ce02db541adbf9d3a6604caf5fbdaae6'
$nodeVersion = 'v24.22.2'
$ownedProcesses = [System.Collections.Generic.List[System.Diagnostics.Process]]::new()
$environmentKeys = @('JAVA_HOME','PATH','BASE_URL','API_BASE_URL','ALLOW_MUTATIONS','BOOKER_USERNAME','BOOKER_PASSWORD','dbRefresh','NEXT_TELEMETRY_DISABLED')
$savedEnvironment = @{}
foreach ($key in $environmentKeys) { $savedEnvironment[$key] = [Environment]::GetEnvironmentVariable($key, 'Process') }

function Invoke-CheckedNative {
    param([string]$Program, [string[]]$Arguments, [string]$Log)
    $previousPreference = $ErrorActionPreference
    try {
        $ErrorActionPreference = 'Continue'
        & $Program @Arguments *> $Log
        $nativeExit = $LASTEXITCODE
    } finally { $ErrorActionPreference = $previousPreference }
    if ($nativeExit -ne 0) { throw "$Program failed (exit $nativeExit). See $Log" }
}
function Wait-Http {
    param([string]$Url, [int]$Seconds = 180)
    $deadline = [DateTime]::UtcNow.AddSeconds($Seconds)
    do {
        try {
            $response = Invoke-WebRequest -UseBasicParsing -Uri $Url -TimeoutSec 5
            if ($response.StatusCode -eq 200) { return }
        } catch { }
        Start-Sleep -Milliseconds 500
    } while ([DateTime]::UtcNow -lt $deadline)
    throw "Application readiness timeout: $Url. Inspect .tools/app/run-logs."
}
function Find-JavaHome {
    param([string]$Explicit, [int]$Major)
    $candidates = @($Explicit, $env:JAVA_HOME)
    $candidates += @(Get-ChildItem -LiteralPath $toolsRoot -Directory -ErrorAction SilentlyContinue | Where-Object Name -like "jdk-$Major*" | ForEach-Object FullName)
    $javaFolder = Join-Path $env:ProgramFiles 'Java'
    $candidates += @(Get-ChildItem -LiteralPath $javaFolder -Directory -ErrorAction SilentlyContinue | ForEach-Object FullName)
    foreach ($candidate in $candidates) {
        if ($candidate -and (Test-Path -LiteralPath (Join-Path $candidate 'release'))) {
            if ((Get-Content -LiteralPath (Join-Path $candidate 'release') -Raw) -match "JAVA_VERSION=`"$Major[.`"]") {
                return (Resolve-Path -LiteralPath $candidate).Path
            }
        }
    }
    throw "JDK $Major required. Pass -$(if ($Major -eq 17) {'TestJavaHome'} else {'AppJavaHome'}) with its installation path."
}

Push-Location $projectRoot
try {
    New-Item -ItemType Directory -Force $toolsRoot | Out-Null
    $TestJavaHome = Find-JavaHome $TestJavaHome 17
    $AppJavaHome = Find-JavaHome $AppJavaHome 26
    $maven = (Get-Command mvn.cmd -ErrorAction Stop).Source
    # Refuse only active listeners. A bind probe is unreliable immediately after Windows releases a port.
    $listeners = Get-NetTCPConnection -State Listen -ErrorAction SilentlyContinue |
        Where-Object { $_.LocalPort -in (3000..3006) }
    if ($listeners) {
        $occupied = ($listeners | Sort-Object LocalPort | ForEach-Object { $_.LocalPort }) -join ', '
        throw "Application port(s) already in use: $occupied. Stop the owning local application before running this script."
    }
    $appCache = Join-Path $toolsRoot 'app'
    New-Item -ItemType Directory -Force $appCache | Out-Null
    $appRoot = Join-Path $appCache 'mwinteringham-restful-booker-platform-efe2deb'
    if (!(Test-Path -LiteralPath (Join-Path $appRoot 'pom.xml'))) {
        Write-Host "Downloading official application revision $revision"
        $archive = Join-Path $appCache 'source.zip'
        Invoke-WebRequest -UseBasicParsing "https://api.github.com/repos/mwinteringham/restful-booker-platform/zipball/$revision" -OutFile $archive
        Expand-Archive -LiteralPath $archive -DestinationPath $appCache -Force
    }
    $nodeRoot = Join-Path $toolsRoot "node-$nodeVersion-win-x64"
    if (!(Test-Path -LiteralPath (Join-Path $nodeRoot 'node.exe'))) {
        $archiveName = "node-$nodeVersion-win-x64.zip"
        $archive = Join-Path $toolsRoot $archiveName
        Invoke-WebRequest -UseBasicParsing "https://nodejs.org/dist/$nodeVersion/$archiveName" -OutFile $archive
        $checksums = (Invoke-WebRequest -UseBasicParsing "https://nodejs.org/dist/$nodeVersion/SHASUMS256.txt").Content
        $line = $checksums -split "`n" | Where-Object { $_.TrimEnd().EndsWith(" $archiveName") }
        $expected = ($line -split '\s+')[0]
        if (!$expected -or (Get-FileHash -LiteralPath $archive -Algorithm SHA256).Hash -ne $expected) { throw 'Node checksum mismatch' }
        Expand-Archive -LiteralPath $archive -DestinationPath $toolsRoot -Force
    }
    $env:PATH = "$nodeRoot;$env:PATH"
    $env:NEXT_TELEMETRY_DISABLED = '1'
    $env:JAVA_HOME = $AppJavaHome
    $assets = Join-Path $appRoot 'assets'
    if (!$SkipBuild) {
        Write-Host 'Building official backend services (Java 26); logs under .tools/app.'
        Invoke-CheckedNative $maven @('-B','-ntp','-f',(Join-Path $appRoot 'pom.xml'),'-pl','auth,booking,room,report,branding,message','-DskipTests','package') (Join-Path $appCache 'backend-build.log')
        Write-Host 'Installing locked frontend dependencies.'
        Invoke-CheckedNative (Join-Path $nodeRoot 'npm.cmd') @('ci','--prefix',$assets,'--no-audit','--no-fund') (Join-Path $appCache 'frontend-install.log')
    }
    $logs = Join-Path $appCache 'run-logs'
    New-Item -ItemType Directory -Force $logs | Out-Null
    $env:dbRefresh = '0'
    foreach ($service in @('auth','booking','room','report','branding','message')) {
        $jar = Get-ChildItem (Join-Path $appRoot "$service/target") -Filter '*-exec.jar' | Select-Object -First 1
        if (!$jar) { throw "Missing executable JAR for $service. Run without -SkipBuild." }
        $process = Start-Process -FilePath (Join-Path $AppJavaHome 'bin/java.exe') -ArgumentList @('-jar',('"' + $jar.FullName + '"'),'--spring.profiles.active=dev','--server.address=127.0.0.1') -WorkingDirectory $appRoot -WindowStyle Hidden -PassThru -RedirectStandardOutput (Join-Path $logs "$service.stdout.log") -RedirectStandardError (Join-Path $logs "$service.stderr.log")
        $ownedProcesses.Add($process)
    }
    foreach ($entry in @(@(3004,'auth'),@(3000,'booking'),@(3001,'room'),@(3005,'report'),@(3002,'branding'),@(3006,'message'))) {
        Wait-Http "http://127.0.0.1:$($entry[0])/$($entry[1])/actuator/health"
    }
    $nextCli = Join-Path $assets 'node_modules/next/dist/bin/next'
    $frontend = Start-Process -FilePath (Join-Path $nodeRoot 'node.exe') -ArgumentList @(('"' + $nextCli + '"'),'dev','--hostname','127.0.0.1','--port','3003') -WorkingDirectory $assets -WindowStyle Hidden -PassThru -RedirectStandardOutput (Join-Path $logs 'frontend.stdout.log') -RedirectStandardError (Join-Path $logs 'frontend.stderr.log')
    $ownedProcesses.Add($frontend)
    Wait-Http 'http://127.0.0.1:3003/api/room'
    # Warm the page compiler before Selenium's page-load timeout starts.
    Wait-Http 'http://127.0.0.1:3003/'
    $env:BASE_URL = 'http://127.0.0.1:3003'
    $env:API_BASE_URL = 'http://127.0.0.1:3003/api'
    $env:ALLOW_MUTATIONS = 'true'
    # Read documented defaults only for the disposable instance started by this script.
    $readme = Get-Content -LiteralPath (Join-Path $appRoot 'README.md') -Raw
    $env:BOOKER_USERNAME = [regex]::Match($readme, '(?m)^\s*[*-]\s*Username:\s*(\S+)').Groups[1].Value
    $env:BOOKER_PASSWORD = [regex]::Match($readme, '(?m)^\s*[*-]\s*Password:\s*(\S+)').Groups[1].Value
    if (!$env:BOOKER_USERNAME -or !$env:BOOKER_PASSWORD) { throw 'Official README login defaults could not be read' }
    $env:JAVA_HOME = $TestJavaHome
    $headless = if ($Headed) { 'false' } else { 'true' }
    Write-Host "Running $Groups on isolated localhost (Java 17)."
    $ErrorActionPreference = 'Continue'
    & $maven -B -ntp test "-Dgroups=$Groups" "-Dheadless=$headless" '-Dbase.url=http://127.0.0.1:3003' '-Dapi.base.url=http://127.0.0.1:3003/api' '-Dallow.mutations=true' '-Dapi.contract=rewrites'
    $testExit = $LASTEXITCODE
    $ErrorActionPreference = 'Stop'
    if ($testExit -ne 0) { throw "Tests failed (exit $testExit). Inspect target/reports/extent.html and target/surefire-reports." }
    Write-Host 'Tests passed. Report: target/reports/extent.html'
} finally {
    $ErrorActionPreference = 'Continue'
    foreach ($process in $ownedProcesses) {
        if (!$process.HasExited) { & taskkill.exe /PID $process.Id /T /F *> $null }
    }
    foreach ($key in $environmentKeys) { [Environment]::SetEnvironmentVariable($key, $savedEnvironment[$key], 'Process') }
    Pop-Location
}
