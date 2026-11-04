# Project 03 — API + UI Hybrid Test Automation

Independent Maven framework for the Restful Booker hotel demo. Tests cover room inventory, booking contracts, guest validation and propagation of the **same generated room ID** between API and browser. This project complements SauceDemo and OrangeHRM without depending on either.

## Setup and execution

Install JDK 17, Maven 3.9+, and Chrome. Selenium Manager resolves the driver; first execution requires internet access. Dependencies are pinned: Selenium 4.27.0, REST Assured 5.5.0, TestNG 7.10.2, Jackson 2.18.2, Extent 5.1.2 and SLF4J 2.0.16. These are selected compatible releases, not a claim to be the newest.

For reproducible full coverage on Windows, also install JDK 26 for the **application**, then run:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Run-LocalRegression.ps1
# Subsequent runs can reuse the built application:
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Run-LocalRegression.ps1 -SkipBuild -Headed
```

The runner downloads the pinned official application and checksum-verified Node 24 into ignored `.tools/`, builds the six services, installs locked frontend dependencies, waits for readiness, and executes the selected suite on Java 17. It refuses occupied ports 3000–3006, binds the application to localhost, disables resets, uses documented credentials only inside this disposable instance, and stops only its own processes in `finally`. The PowerShell execution policy option applies to that process only. Pass `-TestJavaHome` and `-AppJavaHome` if automatic JDK discovery cannot find your installations; `-Groups api|ui|integration|regression|smoke` selects a suite.

The direct Maven commands below use the configured environment (the public demo by default), whose availability can differ from the isolated local application:

```powershell
mvn clean compile
mvn test-compile
mvn test                         # smoke: API room retrieval + browser home
mvn test -Dgroups=api
mvn test -Dgroups=ui
mvn test -Dgroups=integration
mvn test -Dgroups=regression
mvn test -Dgroups=ui -Dheadless=false
```

Groups: `smoke`, `regression`, `api`, `ui`, `integration`, `negative`. Integration tests are not assigned `api`/`ui`, keeping layer-only commands independent. Execution is serial with no retries or order dependencies. A skipped write test is not successful end-to-end validation.

## Configuration

JVM property > environment > packaged default. Configure both URLs for another environment.

| Property | Environment | Default |
|---|---|---|
| `base.url` | `BASE_URL` | `https://automationintesting.online` |
| `api.base.url` | `API_BASE_URL` | `https://automationintesting.online/api` |
| `headless` | `HEADLESS` | `true` |
| `wait.seconds` | `WAIT_SECONDS` | `20` |
| `allow.mutations` | `ALLOW_MUTATIONS` | `false` |
| `api.contract` | `API_CONTRACT` | `rewrites` |

Use `-Dheadless=false` or `HEADLESS=false` for headed mode. Secrets use only environment variables `BOOKER_USERNAME` and `BOOKER_PASSWORD`. Missing credentials or disabled writes explicitly skip dependent tests. Incorrect supplied credentials fail authentication.

```powershell
# Authorized disposable environment. Supply secrets through your secret manager.
$env:BASE_URL = 'http://localhost:3003'
$env:API_BASE_URL = 'http://localhost:3003/api'
$env:ALLOW_MUTATIONS = 'true'
# Populate BOOKER_USERNAME and BOOKER_PASSWORD in this process environment.
mvn test -Dgroups=regression
```

Follow the [official application's local build instructions](https://github.com/mwinteringham/restful-booker-platform#building-locally) separately; it currently requires its own newer Java and Node runtimes. The tests target Java 17. The unrelated `restful-booker` API has different contracts and cannot replace this target.

## Architecture and hybrid workflow

`config` loads settings; `api` encapsulates REST Assured specifications and authenticated domain clients; Jackson `models` are immutable records; `pages` encapsulate Selenium selectors and waits. Test fixtures own UUID-marked resources and cleanup. Assertions remain in tests and API response contracts.

HYB-01 creates a unique room through the API, discovers its ID via room listing (the static handler returns only `{success:true}`), opens `/reservation/{generated-id}` and checks that room's exact description, heading and guest form. It updates the same room through the API, checks persisted data and the revised reservation UI description, then deletes the owned room. The home page deliberately shows only the first three rooms, so it is not used to find newly created rooms.

HYB-02 creates an isolated room by API, submits a valid booking through that room's reservation UI, checks confirmation, then verifies the generated booking ID, guest, room and dates through the authenticated API. It removes notifications matching its unique guest name, the booking and the room. API-08 separately covers booking CRUD and duplicate dates without optional contact fields.

The official frontend's rewrites run before dynamic route handlers. Consequently static creation/login paths use frontend JSON responses while dynamic room PUT/DELETE and booking DELETE expose backend 202 responses. `ApiContract.REWRITES` models these exact verified statuses. The optional `proxy` contract models deployments that use dynamic handlers instead; assertions are selected explicitly by configuration and never inferred from an unexpected response. See API_ENDPOINTS for the response matrix.

## Reporting and diagnostics

Open `target/reports/extent.html`. Surefire XML/text is in `target/surefire-reports/`; failure screenshots are in `target/screenshots/`, linked relatively from Extent. Archive the whole diagnostics tree before the next run, which replaces the report. Reports include test descriptions/groups, status, timestamps/duration, environment, browser and stack traces. API logging allows method, path, status and timing only; all headers/bodies are omitted. Generated artifacts and `.env` files are ignored. Review screenshots before publishing portfolio evidence.

## Monorepo CI installation

This workspace is a starter export **without `.git` or the parent monorepo**. No repository was initialized and no existing project was changed. Copy this project to `api-ui-hybrid-testing/` under the verified portfolio root. Install [ci/api-ui-hybrid-tests.yml](ci/api-ui-hybrid-tests.yml) at **`<repository-root>/.github/workflows/api-ui-hybrid-tests.yml`**, checking for an existing file first. Its current location is an installation template, not an active workflow; nested project workflows are not discovered by GitHub.

After placing the project in the existing monorepo, `scripts/Install-MonorepoWorkflow.ps1` verifies the Git root and correct project location, refuses to overwrite an existing workflow, and installs the template in the root `.github/workflows` directory.

Automatic CI runs execute isolated **full regression** on Windows with Java 17 tests, Java 26 application services, Maven caching, Chrome headless and diagnostics on failure. Manual dispatch selects groups and optionally enables a separate read-only public smoke job on Ubuntu. It uses no account secrets for local regression; documented local demo defaults are read at runtime from the official application README. Public writes remain disabled.

## Evidence, limitations and security

See [execution record](docs/EXECUTION_RECORD.md), [API provenance](docs/API_ENDPOINTS.md), [test plan](docs/TEST_PLAN.md), [scenario matrix](docs/TEST_CASES.md) and [architecture](docs/FRAMEWORK_ARCHITECTURE.md).

Final Java 17 local regression: **13 passed, 0 failed, 0 skipped**, including both hybrid workflows and resource cleanup. Clean compile and test compilation also passed. The [execution record](docs/EXECUTION_RECORD.md) distinguishes this verified run from historical public-demo failures. The [delivery manifest](docs/DELIVERY_MANIFEST.md) lists project files.

The public demo resets and can differ from upstream. Known 500 responses are explicitly characterized; they are not desired business behavior. Contract failures remain visible. Prefer the isolated runner for full regression and low public traffic. Cleanup errors surface; a create/discovery network failure may require manual recovery using the logged UUID marker. Never delete seed data or other users' resources. Nothing has been committed or pushed.
