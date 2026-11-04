# Execution record

Validated 2026-11-02, Asia/Shanghai. **Final isolated regression: 13 passed, 0 failed, 0 skipped.** Historical public-demo failures are retained below and are not presented as current local results.

## Final isolated local validation

The pinned official application at `efe2deb8ce02db541adbf9d3a6604caf5fbdaae6` was downloaded as a source archive into ignored `.tools/` (no nested Git repository). Its six Spring services built successfully on Java 26.0.2.1. The locked frontend dependencies installed successfully using checksum-verified Node 24.22.2, and the actual Next.js frontend ran on localhost. Application resets were disabled. Credentials were read from its official README into process environment variables, never written into framework files.

| Command | Verified outcome |
|---|---|
| `mvn -B -ntp clean compile` using Java 17 | PASS; 11 framework Java files, release 17 |
| `mvn -B -ntp test-compile` using Java 17 | PASS; 8 test/support Java files |
| `powershell.exe -NoProfile -ExecutionPolicy Bypass -File scripts/Run-LocalRegression.ps1` | Full bootstrap exercised: backend build, locked npm install, service startup, local credentials and all 13 tests executed; exposed two incorrect room-update body assertions subsequently fixed |
| `powershell.exe -NoProfile -ExecutionPolicy Bypass -File scripts/Run-LocalRegression.ps1 -SkipBuild` after correction | **Exit 0; 13 tests, 13 passed, 0 failed, 0 skipped** |

The final Maven test phase finished at **19:05:50 UTC+08:00** on 2026-11-02. It used Temurin Java 17.0.20.1, Chrome 156.0.8078.79, localhost URLs and the explicit `rewrites` contract. All API-01–API-08, UI-01–UI-03, HYB-01 and HYB-02 passed. This includes authenticated room/booking CRUD, duplicate-date rejection, guest form validation, API room updates displayed in the same reservation UI, valid UI booking confirmation and API persistence verification.

Final evidence: [Extent report](../target/reports/extent.html), [archived final Extent report](../target/validation/local-verified/reports/extent.html), `target/validation/local-verified/maven.log`, and its Surefire XML/text. The XML was inspected directly: `total=13 passed=13 failed=0 skipped=0`. The Extent report records Java 17 and the selected contract.

Cleanup evidence in the final log includes DELETE `/api/message/2` → 202, DELETE `/api/booking/5` → 202, and DELETE `/api/room/7` → 202 for HYB-02's uniquely owned data. Booking absence and room absence were then checked via authenticated booking listing and public room listing. Other write tests also completed their cleanup without errors. After the runner exited, bind probes confirmed that **all ports 3000–3006 were released**, establishing that its owned application processes stopped. The runner restored its process environment variables.

Two corrections were required by actual execution, rather than assumed behavior:

1. The official Next.js array rewrites run before dynamic route handlers. Static routes return frontend JSON, while dynamic mutations expose exact backend statuses/bodies. `ApiContract.REWRITES` now validates these precisely, including room PUT 202 with a room object and DELETE 202 with an empty body.
2. `Availability.tsx` renders only `rooms.slice(0,3)`. Generated rooms cannot be asserted on a new home card; both genuine hybrid workflows instead use the generated room's reservation URL and its exact unique description.

The unknown-room and malformed-JSON cases remain explicit characterization of existing 500 behavior, not claims that those application defects were repaired. The public demo was not changed to obtain the green local result.

## Environment

Windows 10 x64, Maven 3.9.16, Chrome 156.0.8078.79. The host initially had only Oracle Java 26.0.2.1. An official Eclipse Adoptium Temurin **JDK 17.0.20.1+1** was downloaded from the Adoptium API into ignored `.tools/`, and its SHA-256 was checked against official package metadata before extraction. Final validation used that JDK through process-local `JAVA_HOME`. No machine-wide Java configuration was changed. `javap` confirms class major version **61** (Java 17).

Dependencies resolved from Maven Central. Selenium Manager resolved ChromeDriver 156.0.8078.80 and Chrome actually launched. Selenium 4.27 emits a CDP-version warning for Chrome 156; this framework uses W3C WebDriver rather than CDP features. The home-page browser test passed.

## Historical initial public-demo Java 17 quality gates

| Command | Result | Evidence |
|---|---|---|
| `mvn -B -ntp clean compile` | PASS; 10 framework Java files compiled with release 17 | `target/validation/compile.log` |
| `mvn -B -ntp test-compile` | PASS; 8 test/support Java files compiled | `target/validation/test-compile.log` |
| `mvn -B -ntp test -Dgroups=api` | Exit 1; 8 selected, 4 passed, 2 failed, 2 skipped | `target/validation/api/` |
| `mvn -B -ntp test -Dgroups=ui` | Exit 1; 3 selected, 1 passed, 2 failed | `target/validation/ui/` |
| `mvn -B -ntp test -Dgroups=integration` | Exit 0; 2 selected, both skipped; no business workflow executed | `target/validation/integration/` |

The initial public-demo layer commands selected **13 scenarios: 5 passed, 4 failed, 4 skipped**. Suite group selection was checked against the archived TestNG XML. These are separate historical invocations, not a fabricated aggregate report. They used the original handler-only contract assumption; the later source/routing corrections and successful isolated run above supersede that implementation. Archives remain under `target/validation/api`, `ui`, and `integration`.

## Historical public-demo scenario outcomes

| Scenario | Outcome | Finding |
|---|---|---|
| API-01 | FAIL | Room list/schema returned 200 and validated; detail `/api/room/1` returned 500 instead of 200 |
| API-02 | PASS | Unauthenticated room creation rejected with 401 |
| API-03 | PASS (characterization) | Unknown room returned current proxy 500; this documents a defect, not correct business behavior |
| API-04 | SKIP | Writes disabled; credentials also not configured |
| API-05 | PASS | Missing booking fields returned 400 with validation errors |
| API-06 | PASS (characterization) | Malformed JSON returned current proxy 500; this documents a defect |
| API-07 | FAIL | Unauthenticated booking retrieval returned 500 instead of source-derived 401 |
| API-08 | SKIP | Writes disabled; credentials also not configured |
| UI-01 | PASS | Public home rendered room cards and booking links |
| UI-02 | FAIL | Reservation page stayed on loading spinner; `#doReservation` did not render within 20 seconds |
| UI-03 | FAIL | Same reservation loading failure; guest validation could not execute |
| HYB-01 | SKIP | Write gate skipped before browser startup |
| HYB-02 | SKIP | Write gate skipped before browser startup |

The room-detail failure was consistent with the reservation spinner. Source verified the selector existed; screenshot inspection showed loading rather than a different guest form. The server-side cause of the public deployment's 500 has not been established and was not repaired inside the test framework. All guest-form scenarios subsequently passed on the real isolated application.

## Reporting and screenshot verification

Extent HTML was generated for each invocation. Inspection confirmed duration, failure stacks, environment/browser version and relative screenshot references. Both failing UI tests produced nonempty PNGs; the loading-spinner screenshot was visually inspected. The Java 17 UI report records Java 17.0.20.1. See [UI Extent report](../target/validation/ui/reports/extent.html), [API Extent report](../target/validation/api/reports/extent.html) and [integration skip report](../target/validation/integration/reports/extent.html). Keep each archive's `reports/` and `screenshots/` siblings together for links to work.

An earlier Java 26 full-regression run selected the first 12 scenarios: 5 passed, 4 failed, 3 skipped. Its artifacts are preserved in `target/validation/initial-regression/`. Intermediate local runs documenting routing/card-discovery corrections are archived in `target/validation/local-first/` and `target/validation/local-routing/`. The final isolated Java 17 run above supersedes them and successfully executed authenticated creation, updates and cleanup.

## Security and repository review

Source review found environment lookups for credentials and no credential/token literals. API logging intentionally excludes headers and bodies. `.gitignore` covers `target/`, `.tools/`, logs, `.env*`, reports and screenshots. The source manifest excludes generated diagnostics and the downloaded JDK.

Final static checks also passed: both PowerShell scripts parsed without syntax errors; workflow YAML parsed with its isolated-regression and optional public-smoke jobs present; the workflow installer correctly exited before writing anything when no Git root was found. The final manifest lists 34 implementation/documentation files, including 19 Java files. GitHub-hosted workflow execution has not been performed.

`git rev-parse --show-toplevel` and `git status --short` both report **not a Git repository**. Therefore no staging/diff inspection is possible here; no Git repository, commits or pushes were created. The workspace contained only the starter and no SauceDemo/OrangeHRM directory. The existing parent projects were not accessed or modified.

## Remaining environment work

1. Integrate this directory into the actual existing portfolio checkout and review its Git diff/status there.
2. Install `ci/api-ui-hybrid-tests.yml` at the verified repository root's `.github/workflows/api-ui-hybrid-tests.yml`; the template is not active in this export.
3. Run the installed workflow on GitHub after monorepo integration. Its local runner has been exercised here, but GitHub-hosted execution is not claimed.
4. Optional: recheck public deployment failures after application recovery. Full regression is reproducible locally and does not depend on their recovery.
5. Archive and sanitize selected genuine evidence before any later commit. No approval to commit or push has been requested or assumed.
