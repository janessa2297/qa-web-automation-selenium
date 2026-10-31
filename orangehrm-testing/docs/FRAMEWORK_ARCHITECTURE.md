# Framework Architecture

```text
TestNG suite (testng.xml / write-testng.xml)
   |
   +--> Test classes (assertions, business flow)
   |       |
   |       +--> Page Objects (locators, actions, explicit waits)
   |                   |
   |                   +--> DriverFactory (thread-local WebDriver)
   |                              |
   |                              +--> Selenium Manager / browser
   |
   +--> ExtentReportListener (results, step logs, embedded screenshots)
   |
   +--> Maven Surefire (test execution and XML reports)
```

## Design decisions

- **POM:** Page classes do not contain TestNG assertions; tests make the decisions about pass/fail.
- **Driver lifecycle:** `@BeforeMethod` creates and opens a browser, `@AfterMethod` quits it; tests do not leak state to one another.
- **Explicit waits:** No `Thread.sleep()` and no implicit wait; reduces race conditions with OrangeHRM's asynchronous SPA pages.
- **Configuration:** Defaults are checked in, private credentials are environment variables.
- **Execution:** Thread-local WebDriver and reporting scope; test-class page/driver fields are shared within each instance. Suites run sequentially. Parallel method execution requires refactoring those fields first.
- **Write guardrail:** Default suite is read-only. Employee creation/deletion requires *both* selection of a separate suite and the opt-in property.
- **Screenshots:** Captured on success, failure, and configuration failure when a browser is available. PNGs are saved under `target/screenshots` and embedded as Base64 in the Extent HTML report. Generated output is excluded from Git.
- **Report detail:** Scenario descriptions and categories identify coverage. New workflows use TestNG Reporter step logs; the listener transfers them into Extent. Configuration failures receive separate report entries, so setup failures remain visible even if a test is skipped. Raw DataProvider values are not included in Extent display names.

## Known limitations

- All 18 default-suite cases were validated in Chrome headless on October 30, 2026. Edge, Firefox, and employee creation/deletion remain unverified.
- End-to-end cleanup is best effort; if the demo resets or concurrent edits happen, data may remain.
- The HTML report is written when the suite finishes and replaces the previous report. Spark CSS/JavaScript assets may require internet access.
- Parallelization should be introduced after a controlled test environment and test data isolation strategy are available.

