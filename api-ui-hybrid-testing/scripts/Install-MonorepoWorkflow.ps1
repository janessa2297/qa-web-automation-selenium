[CmdletBinding()]
param()
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$ErrorActionPreference = 'Continue'
$repositoryRoot = & git -C $projectRoot rev-parse --show-toplevel 2>$null
$gitExit = $LASTEXITCODE
$ErrorActionPreference = 'Stop'
if ($gitExit -ne 0) { throw 'This project must first be placed inside the existing portfolio Git checkout. No repository will be created.' }
$repositoryRoot = [IO.Path]::GetFullPath($repositoryRoot.Trim())
$expectedProject = Join-Path $repositoryRoot 'api-ui-hybrid-testing'
if ([IO.Path]::GetFullPath($projectRoot) -ne [IO.Path]::GetFullPath($expectedProject)) {
    throw "Expected project location: $expectedProject. Refusing to install a nested or incorrectly rooted workflow."
}
$destination = Join-Path $repositoryRoot '.github/workflows/api-ui-hybrid-tests.yml'
if (Test-Path -LiteralPath $destination) { throw "Workflow already exists: $destination. Review it manually; this script will not overwrite it." }
New-Item -ItemType Directory -Force (Split-Path -Parent $destination) | Out-Null
Copy-Item -LiteralPath (Join-Path $projectRoot 'ci/api-ui-hybrid-tests.yml') -Destination $destination
Write-Host "Installed workflow: $destination. No commit or push was performed."
