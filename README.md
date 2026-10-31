# 🧪 QA Web Automation — Selenium Portfolio

### Java | Selenium WebDriver | TestNG | Maven | Page Object Model | CI/CD

A collection of web UI automation testing projects showcasing practical **QA Automation Engineering and Software Development Engineer in Test (SDET)** skills.

This repository demonstrates the design and implementation of maintainable automation frameworks, functional and regression testing, end-to-end validation, test reporting, and software quality engineering practices across different web applications.

---

## 👨‍💻 About This Portfolio

I am a QA Automation Engineer with **5+ years of software quality engineering experience and 7+ years of overall IT experience**, specializing in test automation, quality assurance, and software engineering.

My technical background includes Java, Selenium WebDriver, TestNG, Maven, API testing, test framework development, and enterprise software quality assurance.

This portfolio highlights my ability to:

- Design and implement scalable Selenium automation frameworks.
- Develop reusable Page Object Model components.
- Create comprehensive functional, negative, and regression test scenarios.
- Automate critical end-to-end business workflows.
- Implement automated reporting and failure diagnostics.
- Apply test automation best practices and maintainable coding principles.
- Integrate automation into CI/CD workflows.

---

## 📂 Automation Projects

This repository contains two implemented Selenium automation projects, each maintained in a dedicated folder.

| Project | Application Domain | Technologies | Status |
|---|---|---|---|
| [01 — SauceDemo](saucedemo-testing/) | E-Commerce | Java, Selenium, TestNG, Maven | Completed |
| [02 — OrangeHRM](orangehrm-testing/) | Human Resource Management | Java, Selenium, TestNG, Maven | Completed |

---

## 🛒 Project 01 — SauceDemo E-Commerce Automation

**[View SauceDemo Project](saucedemo-testing/)**

A comprehensive Selenium automation framework developed to validate key e-commerce workflows using the SauceDemo demonstration application.

### Functional Coverage

- Login authentication and validation
- Positive and negative login scenarios
- Product inventory and sorting
- Product details validation
- Shopping cart operations
- Checkout form validation
- Order summary and price calculations
- Application navigation and logout
- End-to-end purchase workflows

### Framework Highlights

- Java 17, Selenium WebDriver, TestNG, Maven
- Page Object Model (POM)
- Thread-local WebDriver management
- Parallel TestNG test-context execution
- Reusable configuration and test utilities
- Extent HTML execution reporting
- Automated failure diagnostics
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
| Execution Date | October 28, 2026 |

The results represent a recorded test execution and may differ on subsequent runs.

**[View SauceDemo Source Code, Documentation & Test Evidence](saucedemo-testing/)**

---

## 👥 Project 02 — OrangeHRM Automation Framework

**[View OrangeHRM Project](orangehrm-testing/)**

A Java-based Selenium automation framework developed to validate authentication, dashboard navigation, employee management, and Human Resource Management System workflows using the OrangeHRM demonstration application.

### Functional Coverage

- Login page validation
- Positive and negative authentication scenarios
- Dashboard functionality
- Employee search and filtering
- Employee management workflows
- Session and logout validation
- Employee lifecycle testing

### Framework Highlights

- Java, Selenium WebDriver, TestNG, Maven
- Page Object Model architecture
- Dedicated WebDriver factory
- Reusable framework configuration
- Data-driven login test support
- Extent HTML reporting
- Screenshot capture utilities
- TestNG suite organization
- Test plan and framework architecture documentation

### Test Documentation

The OrangeHRM project includes:

- `README.md` — Framework overview and execution instructions
- `docs/TEST_PLAN.md` — Testing scope and approach
- `docs/FRAMEWORK_ARCHITECTURE.md` — Automation framework architecture
- `testng.xml` — Main test suite configuration
- `write-testng.xml` — Additional employee write-test suite configuration

Test execution results and evidence should be reviewed in the OrangeHRM project documentation.

**[View OrangeHRM Source Code & Framework Documentation](orangehrm-testing/)**

---

## 🏗️ Repository Architecture

Each automation project is organized independently to maintain separation of concerns, support extensibility, and simplify project maintenance.

```text
qa-web-automation-selenium/
│
├── saucedemo-testing/
│   ├── src/
│   ├── screenshots/
│   ├── reports/
│   ├── README.md
│   ├── pom.xml
│   └── testng.xml
│
├── orangehrm-testing/
│   ├── docs/
│   │   ├── FRAMEWORK_ARCHITECTURE.md
│   │   └── TEST_PLAN.md
│   ├── src/
│   ├── README.md
│   ├── pom.xml
│   ├── testng.xml
│   └── write-testng.xml
│
├── .gitignore
└── README.md
```

Both projects use their own Maven configurations, test suites, source code, and framework components.

---

## 🛠️ Technical Skills and Tools

| Category | Technologies / Practices |
|---|---|
| Programming | Java |
| Web UI Automation | Selenium WebDriver |
| Testing Framework | TestNG |
| Build Management | Maven |
| Design Patterns | Page Object Model, Driver Factory |
| Test Types | Functional, Smoke, Regression, Negative, End-to-End |
| Reporting | Extent Reports, Maven Surefire |
| Browser Management | Selenium WebDriver, Thread-Local Driver Handling |
| CI/CD | GitHub Actions (SauceDemo) |
| Version Control | Git, GitHub |
| Documentation | Test Plans, Test Cases, Framework Architecture |

---

## 🧠 Test Automation Methodology

The projects follow a structured quality engineering approach.

### 1. Test Analysis and Planning

Identify critical workflows, business risks, expected behaviors, and application-specific testing requirements.

### 2. Test Scenario Design

Develop positive, negative, functional, regression, and end-to-end test scenarios with meaningful expected results.

### 3. Automation Framework Development

Implement reusable page objects, driver management, configuration handling, and shared utilities.

### 4. Test Implementation

Create maintainable automated test methods with descriptive naming and meaningful assertions.

### 5. Test Execution and Debugging

Execute automated suites, investigate failures, and validate application behavior.

### 6. Reporting and Documentation

Generate execution reports, preserve selected evidence, and document framework design and test coverage.

### 7. Continuous Improvement

Refactor code, improve stability, expand test coverage, and enhance framework capabilities.

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 17 or a version compatible with the individual project
- Apache Maven
- Chrome or another supported browser
- IntelliJ IDEA, VS Code, or Eclipse
- Git

### Clone the Repository

```bash
git clone https://github.com/janessa2297/qa-web-automation-selenium.git
```

### Run SauceDemo Tests

```bash
cd qa-web-automation-selenium/saucedemo-testing
mvn clean test
```

### Run OrangeHRM Tests

From the repository root:

```bash
cd orangehrm-testing
mvn clean test
```

For application-specific configuration, test data requirements, and additional execution commands, refer to each project's README.

---

## 📊 Test Execution Reporting

Automated execution reports provide visibility into test outcomes, scenario-level results, execution statistics, and available diagnostic information.

### SauceDemo

Includes documented execution screenshots and an archived HTML report with Overview, Scenarios, Features, and Analytics sections.

**[View SauceDemo Test Evidence](saucedemo-testing/#-test-execution-evidence)**

### OrangeHRM

Includes an Extent reporting implementation and screenshot capture utilities.

**[View OrangeHRM Reporting Documentation](orangehrm-testing/)**

---

## 🎯 Engineering Principles

The automation frameworks emphasize:

- **Maintainability:** Reusable components and clear separation of concerns.
- **Reliability:** Explicit synchronization and meaningful assertions.
- **Test Independence:** Reduced dependencies between test scenarios.
- **Scalability:** Extensible project and test architecture.
- **Traceability:** Clear test naming, documentation, and reporting.
- **Reproducibility:** Maven-based execution and defined configuration.
- **Code Quality:** Organized source code and consistent framework patterns.

---

## 📈 Continuous Development

Future portfolio improvements may include:

- [ ] Additional CI/CD coverage for OrangeHRM
- [ ] Cross-browser execution evidence
- [ ] Expanded data-driven and negative testing
- [ ] Automated accessibility testing
- [ ] Test execution trend analysis
- [ ] Additional web application automation projects

---

## 📌 Project Navigation

| Project | Documentation |
|---|---|
| SauceDemo Automation | [Open Project README](saucedemo-testing/README.md) |
| OrangeHRM Automation | [Open Project README](orangehrm-testing/README.md) |
| SauceDemo Test Evidence | [View Screenshots](saucedemo-testing/screenshots/) |
| SauceDemo HTML Report | [View Report Files](saucedemo-testing/reports/) |
| OrangeHRM Test Plan | [View Test Plan](orangehrm-testing/docs/TEST_PLAN.md) |
| OrangeHRM Framework Architecture | [View Architecture](orangehrm-testing/docs/FRAMEWORK_ARCHITECTURE.md) |

---

## 📚 Disclaimer

These projects are independent automation portfolio demonstrations developed using publicly accessible demonstration applications.

They are not affiliated with or endorsed by Sauce Labs or OrangeHRM.

---

## ⭐ Portfolio Purpose

This repository showcases my technical capabilities in Selenium WebDriver automation, Java programming, test framework design, test strategy, and software quality engineering.

Each project demonstrates practical implementation of maintainable automated testing solutions across different application domains.

The portfolio will continue to evolve through additional testing scenarios, framework improvements, and engineering projects.
