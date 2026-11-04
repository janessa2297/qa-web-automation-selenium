# Framework architecture

## Layers

`src/main` contains configuration, REST clients/specifications, immutable Jackson models, driver creation and page objects. `src/test` contains TestNG scenarios/assertions, data factories, resource ownership and Extent integration. No application server dependency is added. Maven pins dependency/plugin versions and compiles with release 17.

## Data flow and ownership

Tests call domain clients using fresh request specifications and instance-owned cookie tokens. No static REST Assured base URL/token is mutated. Requests have 15-second connection and 20-second read timeouts. Logs permit method/path/status/timing only; all headers and bodies are omitted. `ApiContract` distinguishes exact `rewrites` versus handler-only `proxy` behavior, explicitly selected by configuration rather than detected by accepting whichever response arrives.

Write gate → credential gate → login → create UUID room → discover generated ID → operations → try-with-resources cleanup. Register booking IDs before assertions. Cleanup attempts every tracked deletion, preserves primary failures using suppressed exceptions, and never deletes arbitrary shared data. Network failures after creation cannot guarantee rollback; the marker supports manual recovery.

Hybrid flow: API-created room → reservation page at generated ID → exact description, heading and guest form → API update of same room → persisted API value → revised reservation UI text → delete owned room. The home page renders only three rooms and cannot reliably expose an additional generated room. HYB-02 uses that reservation page for valid guest submission, confirms it in the UI and reads the same booking through the API.

## Driver lifecycle and waits

`BaseUiTest` creates one instance-owned Chrome driver per test and quits in `alwaysRun` teardown. Serial execution is intentional; parallel support is not claimed. Selenium Manager resolves the driver. Explicit waits cover rendering/clickability; implicit wait is zero, page-load timeout 45 seconds. Page classes own interactions/selectors, while tests own assertions.

## Reporting

The suite registers one Extent listener. Each invocation replaces `target/reports/extent.html`; categories preserve layer/group distinctions. Timestamps, duration, environment, browser and throwable stacks accompany results. UI failure screenshots are captured before browser teardown and linked relatively. Surefire diagnostics remain authoritative for configuration/setup failures. Archive evidence before another invocation.

## CI design

The workflow template runs from `api-ui-hybrid-testing` and uploads diagnostics even on failure. Its automatic Windows job builds the pinned official application with Java 26, runs tests with Java 17 and uses a checksum-verified Node 24 runtime. It caches Maven, runs full isolated regression, and does not need account secrets. A manually enabled Ubuntu job runs only read-only public smoke. It must be installed under the verified parent root's `.github/workflows`. The supplied export has no `.git`; the installer checks the actual repository root/project location and refuses overwrites. Parent Git integration remains an external prerequisite.

`Run-LocalRegression.ps1` bootstraps into ignored `.tools/`, refuses occupied application ports, waits for service health and frontend readiness, reads documented login defaults for its own local instance, forces localhost test URLs and the verified rewrites contract, and restores process environment variables in finally. It records and stops only processes it starts; the upstream script's broad Java/Node termination is not used. Downloaded source is an archive, so no nested Git repository is created.

## Tradeoffs

Static handlers and rewritten dynamic paths expose different contracts. Known 500 cases are narrowly characterized; unauthorized booking retrieval expects precisely 403 for rewrites or 401 for handler-only proxy deployments and fails on drift. No retries conceal defects. Guest names are unique and registered before submission so even a UI confirmation failure triggers discovery/cleanup of persisted bookings and matching notifications. Actual executed coverage is recorded separately.
