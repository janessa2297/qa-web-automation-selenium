# Project 03 — API + UI Hybrid Automation: CI Test Evidence

## Execution overview

| Field | Result |
|---|---|
| Execution date | 2026-11-04 (UTC, as recorded in JUnit reports) |
| CI platform | GitHub Actions |
| Workflow | `API UI hybrid tests` (`api-ui-hybrid-tests.yml`) |
| Git commit | `60f36ac` |
| Job | `local-regression` |
| Job result | Success |
| Test framework | Java, Maven Surefire, TestNG, Selenium, REST Assured |
| Test execution duration | 41.00 seconds (Surefire TestSuite summary) |
| Total tests | **13** |
| Passed | **13** |
| Failed | **0** |
| Errors | **0** |
| Skipped | **0** |
| Pass rate | **100%** |

## Results by suite

| Test suite | Tests | Passed | Failed | Errors | Skipped |
|---|---:|---:|---:|---:|---:|
| `BookingApiTest` | 4 | 4 | 0 | 0 | 0 |
| `RoomApiTest` | 4 | 4 | 0 | 0 | 0 |
| `RoomHybridTest` | 2 | 2 | 0 | 0 | 0 |
| `BookingUiTest` | 3 | 3 | 0 | 0 | 0 |
| **Total** | **13** | **13** | **0** | **0** | **0** |

## Evidence sources

The following files were inspected from the downloaded GitHub Actions artifact `hybrid-local-diagnostics.zip`:

- `surefire-reports/TestSuite.txt` — Maven Surefire summary: 13 tests, zero failures/errors/skips.
- `surefire-reports/testng-results.xml` — TestNG result totals.
- `surefire-reports/junitreports/TEST-com.portfolio.hybrid.api.BookingApiTest.xml`
- `surefire-reports/junitreports/TEST-com.portfolio.hybrid.api.RoomApiTest.xml`
- `surefire-reports/junitreports/TEST-com.portfolio.hybrid.integration.RoomHybridTest.xml`
- `surefire-reports/junitreports/TEST-com.portfolio.hybrid.ui.BookingUiTest.xml`
- `surefire-reports/emailable-report.html` — TestNG HTML report.
- `reports/extent.html` — Extent HTML report.

## CI observations

- GitHub Actions `local-regression` job completed successfully (workflow duration shown in screenshot: 4m 25s).
- Optional `public-smoke` job was skipped, as expected for a push-triggered run.
- The workflow emitted three deprecation warnings for older GitHub Actions versions. These warnings did not fail this run and should be addressed in a separate maintenance change.

## Scope and limitations

These results represent **one successful CI run**, not a guarantee that all application functionality is defect-free. The report covers the 13 automated tests executed in this run. The optional public demo smoke job was not executed.

## GitHub portfolio presentation

Suggested evidence files under `api-ui-hybrid-testing/evidence/`:

- `PROJECT03_CI_TEST_EVIDENCE.md` — this verified execution summary.
- `github-actions-success.png` — screenshot of the completed GitHub Actions run (save separately from GitHub).
- `testng-report.png` — screenshot of the HTML report opened locally (save separately).

Avoid committing raw `target/` output, machine-specific logs, or secrets. The full original diagnostic ZIP remains available as a GitHub Actions artifact for the run while retained.
