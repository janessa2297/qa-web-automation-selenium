# SauceDemo Selenium Automation Framework

Java 17, Selenium WebDriver, TestNG, and Maven tests for the public SauceDemo shopping application.

## Requirements
- JDK 17 or newer
- Maven 3.9 or newer
- Chrome, Edge, or Firefox installed
- Internet access for SauceDemo and Selenium Manager

## Run tests
```bash
mvn clean test
mvn clean test -Dbrowser=chrome -Dheadless=true
mvn clean test -Dbrowser=edge
mvn clean test -Dbrowser=firefox
```

Run the framework tests without launching a browser:
```bash
mvn test -Dtest=ConfigReaderTest,MoneyUtilsTest,ExtentLoggerTest,TestListenerTest
```

The default build runs the framework tests and the complete UI suite, including login, inventory, cart, checkout, menu, and end-to-end purchases. UI test contexts run on two threads, each with its own browser. Parallel execution of methods on the same test instance is not supported.

## Configuration
Defaults live in `src/test/resources/testdata/config.properties`. Override any configuration key with a JVM property, for example `-DexplicitWaitSeconds=20`. Timeout values must be positive integers. Only public demo credentials belong in the configuration file.

Expected catalog values, validation messages, customer fixtures, and the expected tax rate live in `TestData`. These deliberately independent expectations allow tests to detect application regressions.

## Structure
- `src/main/java/com/portfolio/config/`: validated configuration loading
- `src/main/java/com/portfolio/driver/`: browser creation and thread-local cleanup
- `src/main/java/com/portfolio/pages/`: page objects and shared interactions
- `src/main/java/com/portfolio/utils/`: waits, currency parsing, screenshots, and reports
- `src/test/java/com/portfolio/base/`: TestNG browser lifecycle
- `src/test/java/com/portfolio/tests/`: UI assertions and scenarios
- `src/test/java/com/portfolio/listeners/`: Extent reporting and listener verification
- `src/test/resources/`: framework and UI suite definitions

## Reports and CI
Every Maven run writes to a new timestamped folder under `test-reports/`, containing `extent-report.html`, `surefire/index.html`, `surefire/testng-results.xml`, and `screenshots/` when needed.

Open `extent-report.html` for detailed steps and embedded failure screenshots, or `surefire/index.html` for the TestNG summary. The Maven run timestamp uses UTC. Previous runs remain available after `mvn clean test`, because this folder is outside `target/`. An explicit `-DreportPath=...` still overrides the Extent filename; avoid reusing a fixed filename if you want to keep its history.

The Extent report opens on a dashboard with clickable status cards, outcome distribution, feature breakdown, run duration, environment details, and a failure review queue. Scenario details retain their recorded assertions, exceptions, and screenshots; time badges use neutral colors. Its responsive theme lives in `src/main/resources/report/`; all counts come from the recorded test results.

GitHub Actions runs headless Chrome and uploads `test-reports/`, including when tests fail. Browser results depend on public site availability and installed browsers. See `docs/test-cases.md` for scenario documentation.
