#  QA Web Automation — Selenium & API Testing Portfolio

### Java | Selenium WebDriver | REST Assured | TestNG | Maven | Page Object Model | CI/CD

A professional software test automation portfolio demonstrating the design and implementation of maintainable automation frameworks across e-commerce, human resource management, and hotel booking applications.

This repository contains three independent Java automation projects covering UI automation, functional testing, regression testing, API testing, and API + UI integration testing.

---

## 👨‍💻 About Me

I am a QA Automation Engineer with **5+ years of software quality engineering experience and 7+ years of overall IT experience**.

My professional experience includes test automation, functional and regression testing, API testing, software development, test framework design, and quality engineering throughout the Software Development Life Cycle (SDLC).

This portfolio demonstrates my ability to:

- Design and develop reusable test automation frameworks.
- Implement Selenium WebDriver automation using Java.
- Apply Page Object Model and separation of concerns.
- Develop automated API tests using REST Assured.
- Validate end-to-end workflows across application layers.
- Implement meaningful assertions and negative testing.
- Generate execution reports and failure diagnostics.
- Integrate automated tests into CI/CD pipelines.
- Document test strategies, scenarios, and framework architecture.

---

## 📂 Automation Projects

| Project | Domain | Automation Focus | Status |
|---|---|---|---|
| [01 — SauceDemo](saucedemo-testing/) | E-Commerce | UI, Regression, E2E | Completed |
| [02 — OrangeHRM](orangehrm-testing/) | Human Resources | UI, Functional, Employee Management | Completed |
| [03 — API + UI Hybrid](api-ui-hybrid-testing/) | Hotel Booking | API, UI, Integration | Implemented |

Each project is independently organized with its own Maven configuration, source code, test suites, and documentation.

---

## 🛒 Project 01 — SauceDemo E-Commerce Automation

**[View SauceDemo Project](saucedemo-testing/)**

A Java Selenium automation framework designed to validate critical e-commerce workflows using the SauceDemo demonstration application.

### Test Coverage

- Login authentication and negative login scenarios
- Product inventory validation
- Product sorting and product details
- Shopping cart operations
- Checkout form validation
- Order summary and pricing validation
- End-to-end purchase scenarios
- Application navigation and logout

### Framework Features

- Java 17 and Selenium WebDriver
- TestNG and Maven
- Page Object Model
- Reusable page components
- WebDriver lifecycle management
- Parallel test execution support
- Extent HTML reporting
- Failure diagnostics
- GitHub Actions CI/CD integration

### Documented Execution Results

| Metric | Result |
|---|---|
| Total Scenarios | 83 |
| Passed | 83 |
| Failed | 0 |
| Skipped | 0 |
| Pass Rate | 100% |
| Browser | Chrome — Headless |

Results represent a recorded execution, not a guarantee of future outcomes.

**[Explore SauceDemo Documentation and Test Evidence](saucedemo-testing/README.md)**

---

## 👥 Project 02 — OrangeHRM Automation Framework

**[View OrangeHRM Project](orangehrm-testing/)**

A Selenium-based automation framework developed to validate Human Resource Management System functionality using the OrangeHRM demonstration application.

### Test Coverage

- Login page and authentication validation
- Positive and negative login scenarios
- Dashboard functionality
- Employee search and filtering
- Employee management workflows
- Employee lifecycle testing
- Session management and logout

### Framework Features

- Java and Selenium WebDriver
- TestNG and Maven
- Page Object Model
- Driver Factory architecture
- Reusable framework configuration
- Data-driven login test support
- Extent HTML reporting
- Screenshot capture utilities
- Organized TestNG suites
- Test planning and architecture documentation

### Project Documentation

- [Test Plan](orangehrm-testing/docs/TEST_PLAN.md)
- [Framework Architecture](orangehrm-testing/docs/FRAMEWORK_ARCHITECTURE.md)
- [Project README and Execution Evidence](orangehrm-testing/README.md)

---

## 🔗 Project 03 — API + UI Hybrid Automation Framework

**[View API + UI Hybrid Project](api-ui-hybrid-testing/)**

A Java-based hybrid automation framework designed to validate hotel booking functionality across backend APIs and browser-based user interfaces.

This project expands the portfolio beyond traditional Selenium automation by incorporating REST API testing and cross-layer integration testing.

### API Testing

Uses REST Assured to support automated validation of API behavior, including:

- HTTP request and response validation
- Status code assertions
- JSON response validation
- API contract testing
- Positive and negative scenarios
- API test data handling

### UI Testing

Uses Selenium WebDriver to validate browser-based functionality, including:

- Page navigation
- User interactions
- Booking-related UI workflows
- Form validation
- Browser-visible application behavior

### Hybrid Integration Testing

Combines API and UI validation within shared business workflows.

The framework is designed to demonstrate how backend data preparation and verification can complement Selenium browser automation.

### Framework Features

- Java 17
- Selenium WebDriver
- REST Assured
- TestNG
- Maven
- API client abstraction
- Page Object Model
- Configuration management
- Test automation documentation
- CI/CD workflow configuration

### Project Documentation

- [Framework Architecture](api-ui-hybrid-testing/docs/FRAMEWORK_ARCHITECTURE.md)
- [Test Plan](api-ui-hybrid-testing/docs/TEST_PLAN.md)
- [Test Cases](api-ui-hybrid-testing/docs/TEST_CASES.md)
- [API Endpoints](api-ui-hybrid-testing/docs/API_ENDPOINTS.md)
- [Execution Record](api-ui-hybrid-testing/docs/EXECUTION_RECORD.md)
- [Project README](api-ui-hybrid-testing/README.md)

**Note:** Consult the project's execution record for verified test outcomes and environment limitations.

---

## 🏗️ Repository Architecture

```text
qa-web-automation-selenium/
│
├── saucedemo-testing/
│   ├── src/
│   ├── pom.xml
│   ├── testng.xml
│   └── README.md
│
├── orangehrm-testing/
│   ├── docs/
│   ├── src/
│   ├── pom.xml
│   ├── testng.xml
│   ├── write-testng.xml
│   └── README.md
│
├── api-ui-hybrid-testing/
│   ├── ci/
│   ├── docs/
│   ├── scripts/
│   ├── src/
│   ├── pom.xml
│   ├── testng.xml
│   └── README.md
│
├── .gitignore
└── README.md
```

Each project is independently maintainable and has its own test configuration and execution instructions.

---

## 🛠️ Technical Skills Demonstrated

| Category | Technologies / Practices |
|---|---|
| Programming | Java |
| UI Automation | Selenium WebDriver |
| API Automation | REST Assured |
| Test Framework | TestNG |
| Build Management | Maven |
| Design Patterns | Page Object Model, Driver Factory, API Client Layer |
| Test Types | Functional, Regression, Smoke, Negative, End-to-End, Integration |
| Reporting | Extent Reports, Maven Surefire |
| CI/CD | GitHub Actions |
| Version Control | Git, GitHub |
| Documentation | Test Plans, Test Cases, Framework Architecture |

---

## 🧠 Automation Engineering Approach

### 1. Requirements Analysis

Identify critical business workflows, functional requirements, and application risks.

### 2. Test Planning and Design

Create meaningful positive, negative, regression, and integration scenarios.

### 3. Framework Architecture

Develop reusable automation components with clear separation between test logic and application interactions.

### 4. Test Implementation

Implement maintainable automated tests with meaningful assertions and controlled test data.

### 5. Execution and Debugging

Execute tests, investigate failures, and validate expected application behavior.

### 6. Reporting and Traceability

Produce execution reports and document test results, defects, and limitations.

### 7. Continuous Improvement

Refactor automation components, improve stability, and expand meaningful coverage.

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 17 or a compatible project-specific version
- Apache Maven
- Git
- Google Chrome or another supported browser
- VS Code, IntelliJ IDEA, or Eclipse

### Clone Repository

```bash
git clone https://github.com/janessa2297/qa-web-automation-selenium.git
cd qa-web-automation-selenium
```

### Run SauceDemo Tests

```bash
cd saucedemo-testing
mvn clean test
```

### Run OrangeHRM Tests

From the repository root:

```bash
cd orangehrm-testing
mvn clean test
```

### Run API + UI Hybrid Tests

From the repository root:

```bash
cd api-ui-hybrid-testing
mvn clean test
```

The hybrid framework may require additional local application setup and environment configuration. Follow its project README before executing tests.

---

## 📊 Test Execution Evidence

Execution evidence and framework documentation are organized within the individual project directories.

| Project | Evidence and Documentation |
|---|---|
| SauceDemo | [Execution Evidence](saucedemo-testing/README.md) |
| OrangeHRM | [Execution Evidence](orangehrm-testing/README.md) |
| API + UI Hybrid | [Execution Record](api-ui-hybrid-testing/docs/EXECUTION_RECORD.md) |

Execution results are environment-dependent and reflect the conditions documented for each run.

---

## ⚙️ CI/CD and Automation Reliability

The portfolio demonstrates automated testing practices such as:

- Maven-based test execution
- TestNG test suite organization
- Headless browser execution
- Failure reporting
- Environment-based configuration
- Separation of test data and application logic
- GitHub Actions integration

CI/CD capabilities vary by project. Refer to individual project documentation for workflow setup and execution status.

---

## 🎯 Engineering Principles

These frameworks emphasize:

**Maintainability** — Reusable code and clearly defined component responsibilities.

**Reliability** — Explicit synchronization, isolated scenarios, and meaningful assertions.

**Scalability** — Extensible architecture for additional tests and application features.

**Traceability** — Documented test cases, execution records, and reporting.

**Reproducibility** — Maven-based builds and configurable execution environments.

**Security** — Environment-based secrets handling and exclusion of local dependencies and generated artifacts from version control.

---

## 📈 Future Improvements

- [ ] Enable and verify GitHub Actions for the API + UI Hybrid project
- [ ] Expand CI/CD coverage across all projects
- [ ] Add cross-browser execution evidence
- [ ] Introduce additional accessibility testing
- [ ] Improve automated reporting and execution analytics
- [ ] Expand integration and contract testing coverage

---

## 📌 Quick Navigation

| Resource | Link |
|---|---|
| SauceDemo Framework | [View Project](saucedemo-testing/) |
| OrangeHRM Framework | [View Project](orangehrm-testing/) |
| API + UI Hybrid Framework | [View Project](api-ui-hybrid-testing/) |
| OrangeHRM Test Plan | [View Documentation](orangehrm-testing/docs/TEST_PLAN.md) |
| Hybrid Test Cases | [View Test Cases](api-ui-hybrid-testing/docs/TEST_CASES.md) |
| Hybrid Execution Record | [View Execution Results](api-ui-hybrid-testing/docs/EXECUTION_RECORD.md) |

---

## 📚 Disclaimer

These projects are independent educational and professional portfolio demonstrations developed using publicly accessible or locally hosted demonstration applications.

They are not affiliated with or endorsed by the respective application owners.

---

## ⭐ Portfolio Purpose

This repository demonstrates practical capabilities in Java-based test automation, Selenium WebDriver, REST API testing, automation framework design, software testing strategy, and quality engineering.

The goal is to showcase maintainable, well-documented automation solutions that reflect real-world software testing practices.
