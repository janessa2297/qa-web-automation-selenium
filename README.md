# 🧪 SauceDemo — Selenium Test Automation Framework

### Enterprise-Style Web UI Automation | Java • Selenium WebDriver • TestNG • Maven

A structured web automation testing project developed to demonstrate practical Software Quality Assurance (SQA) engineering skills, including functional testing, regression testing, test framework design, and automated test execution.

The framework automates key workflows of [SauceDemo](https://www.saucedemo.com/), a sample e-commerce application, using Java, Selenium WebDriver, TestNG, and Maven.

It is designed around maintainable test architecture, reusable components, and clearly defined test scenarios to support reliable automation and future framework enhancements.

---

## 📌 Project Overview

**Application Under Test:** SauceDemo (Swag Labs)  
**Application Type:** E-Commerce Web Application  
**Automation Type:** Web UI Functional & Regression Testing  
**Programming Language:** Java  
**Automation Framework:** Selenium WebDriver  
**Test Framework:** TestNG  
**Build Management:** Apache Maven  
**Design Pattern:** Page Object Model (POM)  
**Version Control:** Git & GitHub

### Project Objectives

- Automate critical e-commerce workflows and validate expected application behavior.
- Implement reusable, maintainable, and scalable automation components.
- Demonstrate positive, negative, boundary, and data-driven testing approaches.
- Improve test execution consistency through automated assertions.
- Organize test scenarios into smoke and regression suites.
- Generate detailed execution reports for test result analysis.
- Demonstrate practical QA automation engineering and test framework development skills.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java | Automation scripting and framework development |
| Selenium WebDriver | Browser interaction and UI automation |
| TestNG | Test management, assertions, and execution |
| Maven | Dependency management and project build |
| Page Object Model | Separation of page interactions and test logic |
| HTML Execution Reports | Test results, logs, and analytics |
| Git | Source code version control |
| GitHub | Repository hosting and technical portfolio |

---

## 🏗️ Framework Architecture

The automation framework follows the **Page Object Model (POM)** design pattern to separate application page interactions from test execution logic.

### Framework Design Principles

**1. Separation of Concerns**

Page classes manage element locators and browser interactions, while test classes validate application behavior using assertions.

**2. Reusability**

Common browser operations, setup procedures, and page interactions are organized into reusable components to reduce code duplication.

**3. Maintainability**

Application UI changes can be managed within the relevant page classes rather than updating multiple test scripts.

**4. Test Independence**

Test scenarios are designed to execute independently, reducing dependencies between test cases.

**5. Extensibility**

The framework is structured to support additional test cases, page objects, reporting enhancements, and execution configurations.

### Project Structure

```text
saucedemo-testing/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── Page Objects and Framework Components
│   │
│   └── test/
│       └── java/
│           └── Automated Test Classes
│
├── screenshots/
│   ├── Execution Overview Screenshot
│   └── Test Analytics Screenshot
│
├── reports/
│   └── HTML Test Execution Report
│
├── README.md
├── pom.xml
└── testng.xml
```

*Note: The structure above represents the major project components. Refer to the repository's `src/` directory for the actual Java package and class organization.*

---

## 🎯 Test Coverage and Automation Scenarios

The automation strategy covers the application's core e-commerce functionality, with emphasis on business-critical workflows, user interaction validation, and error handling.

### 1. Authentication and Login Testing

| Test Scenario | Validation |
|---|---|
| Login page validation | Verify essential login page elements |
| Valid login | Verify successful authentication |
| Invalid username | Verify authentication error handling |
| Invalid password | Verify authentication error handling |
| Empty credentials | Verify required-field validation |
| Locked-out user | Verify account restriction behavior |
| Logout | Verify successful session termination |
| Multiple user accounts | Validate expected behavior for supported demo users |

### 2. Product Inventory Testing

| Test Scenario | Validation |
|---|---|
| Inventory page | Verify product listing visibility |
| Product information | Verify product names and details |
| Product sorting A–Z | Verify alphabetical ascending order |
| Product sorting Z–A | Verify alphabetical descending order |
| Price sorting low–high | Verify ascending price order |
| Price sorting high–low | Verify descending price order |
| Product navigation | Verify product detail navigation |

### 3. Shopping Cart Testing

| Test Scenario | Validation |
|---|---|
| Add product | Verify item is added successfully |
| Add multiple products | Verify cart handles multiple items |
| Remove product | Verify item removal |
| Cart badge | Verify item count updates |
| Cart contents | Verify selected products appear |
| Continue shopping | Verify navigation back to inventory |

### 4. Checkout Testing

| Test Scenario | Validation |
|---|---|
| Valid checkout | Verify successful order completion |
| Missing first name | Verify required-field validation |
| Missing last name | Verify required-field validation |
| Missing postal code | Verify required-field validation |
| Checkout overview | Verify selected items and pricing |
| Order confirmation | Verify successful checkout message |

### 5. End-to-End Testing

End-to-end scenarios validate complete user journeys across multiple application modules.

**Example business workflow:**

```text
Launch Application
       ↓
Authenticate User
       ↓
View Product Inventory
       ↓
Add Product to Cart
       ↓
Review Shopping Cart
       ↓
Enter Checkout Information
       ↓
Review Order Summary
       ↓
Complete Checkout
       ↓
Validate Order Confirmation
```

**Coverage note:** The scenarios above describe the intended functional coverage. Refer to the committed automated test classes and execution report for the exact scenarios implemented and executed.

---

## 🧠 Automation Testing Strategy

The project applies several QA testing techniques to improve test coverage and reliability.

| Testing Technique | Description |
|---|---|
| Positive Testing | Validates expected behavior using valid input |
| Negative Testing | Validates application handling of invalid input |
| Boundary / Field Validation | Validates required-field and input conditions |
| Functional Testing | Validates individual application features |
| Regression Testing | Verifies existing functionality after changes |
| Smoke Testing | Verifies critical application workflows |
| Data-Driven Testing | Supports validation using multiple input datasets |
| End-to-End Testing | Validates complete business processes |

The approach prioritizes critical workflows before expanding into broader regression coverage.

---

## ⚙️ Prerequisites

Before running the automation project, ensure the following are available:

- Java Development Kit (JDK) compatible with the project configuration
- Apache Maven
- Google Chrome or another browser supported by the configured WebDriver
- IntelliJ IDEA, Eclipse, or another Java-compatible IDE
- Internet connection to access SauceDemo

## 🚀 Getting Started

### Step 1 — Clone the Repository

```bash
git clone https://github.com/<YOUR-GITHUB-USERNAME>/qa-web-automation-selenium.git
```

Replace `<YOUR-GITHUB-USERNAME>` with your actual GitHub username.

### Step 2 — Navigate to the SauceDemo Project

```bash
cd qa-web-automation-selenium/saucedemo-testing
```

### Step 3 — Open the Project

Open the `saucedemo-testing` folder in IntelliJ IDEA or Eclipse.

Allow Maven to download and configure the required dependencies.

### Step 4 — Execute the Automation Tests

From the project directory, run:

```bash
mvn clean test
```

Alternatively, execute the configured TestNG suite using your IDE.

### Step 5 — Review Execution Results

After test execution, inspect the generated test reports and execution logs.

---

## 📊 Test Execution Results

The following results were captured from a SauceDemo automated test execution report.

| Execution Metric | Result |
|---|---|
| Total Scenarios | 83 |
| Passed | 83 |
| Failed | 0 |
| Skipped | 0 |
| Pass Rate | 100% |
| Browser | Google Chrome |
| Execution Mode | Headless |
| Framework | Selenium WebDriver with TestNG |
| Execution Date | October 28, 2026 |
| Execution Duration | Approximately 7 minutes 21 seconds |

**Execution Result:** All 83 reported scenarios passed in the captured execution.

These results represent the recorded execution and do not guarantee that subsequent runs will produce identical results.

---



## 📸 Test Execution Evidence

The following screenshots demonstrate the execution results and test coverage of the SauceDemo Selenium Automation Framework.

### 1. Test Execution Overview

![SauceDemo Overview](./screenshots/SauceDemo_Overview_Report.png)

### 2. Feature Coverage

![SauceDemo Feature Report](./screenshots/SauceDemo_Feature_Report.png)

### 3. Test Execution Analytics

![SauceDemo Analytics](./screenshots/SauceDemo_Analytics_Report.png)





---

## 🌐 How to View the HTML Execution Report

The project includes a browser-viewable test execution report exported in **Webpage, Complete HTML format**.

### Instructions

1. Open the `reports/` directory in this repository.
2. Download the HTML report along with its associated resource folder, if included.
3. Save the HTML file and its resource folder in the same directory.
4. Locate the downloaded HTML file on your computer.
5. **Right-click the HTML file → Open with → Select your preferred browser.**
6. Open it using Google Chrome, Microsoft Edge, or Mozilla Firefox.

### Report Navigation

The report includes the following sections:

| Report Tab | Description |
|---|---|
| Overview | High-level execution summary |
| Scenarios | Individual test scenarios and results |
| Features | Functional test coverage |
| Analytics | Execution metrics, charts, and statistics |

**Important:** Because the report is exported in Webpage, Complete HTML format, keep the HTML file and its supporting resource folder together to preserve formatting, scripts, and other report assets.

Some interactive functionality may also depend on browser security restrictions when opening local files.

---

## 🔍 Test Assertions and Validation

Automated test scenarios use assertions to compare actual application behavior against expected results.

Examples include:

- Validating successful navigation after authentication.
- Verifying error messages for invalid credentials.
- Checking product availability on the inventory page.
- Confirming cart item counts.
- Validating required checkout fields.
- Confirming successful order completion.

Assertions help ensure tests validate meaningful application outcomes rather than simply performing browser actions.

---

## 🧩 Engineering Practices

The framework emphasizes practical software engineering principles.

**Reusable Page Objects**

Encapsulate UI locators and interactions within dedicated page classes.

**Explicit Wait Strategies**

Use synchronization mechanisms to handle dynamic UI behavior and reduce unnecessary timing dependencies.

**Descriptive Test Methods**

Use meaningful method names to communicate the purpose of each test.

**Organized Test Suites**

Group related scenarios by feature and execution purpose.

**Maintainable Code Structure**

Keep test logic, reusable utilities, and page interactions logically separated.

**Execution Evidence**

Maintain reports and screenshots to document automation results.

---

## 📈 Future Enhancements

The following improvements may be introduced as the framework evolves:

- [ ] Expand data-driven testing using TestNG DataProvider.
- [ ] Add cross-browser execution coverage.
- [ ] Implement parallel test execution.
- [ ] Add automatic screenshots on test failures.
- [ ] Integrate enhanced reporting with Allure.
- [ ] Configure or expand GitHub Actions CI/CD execution.
- [ ] Improve test data and configuration management.
- [ ] Add further negative and edge-case scenarios.
- [ ] Add automated test execution documentation.
- [ ] Introduce advanced logging and diagnostic capabilities.

These enhancements will improve scalability, observability, and maintainability.

---

## 💼 Skills Demonstrated

This project provides practical evidence of experience and proficiency in:

- Java Programming
- Selenium WebDriver
- TestNG
- Maven
- Page Object Model
- Test Automation Framework Design
- Functional and Regression Testing
- Positive and Negative Testing
- Test Case Design
- Automated Assertions
- Browser Automation
- Test Execution Reporting
- Git and GitHub
- Software Quality Engineering

---

## 📚 Application Reference

**Application Under Test:** [SauceDemo — Swag Labs](https://www.saucedemo.com/)

SauceDemo is a demonstration e-commerce application used for practicing and evaluating software testing approaches.

This project is an independent educational and professional portfolio demonstration and is not affiliated with Sauce Labs.

---

## 👨‍💻 Portfolio Purpose

This project is part of a broader Software Quality Engineering and Test Automation portfolio.

It demonstrates the application of automation framework design, functional test validation, reusable software components, and structured QA practices to a realistic e-commerce workflow.

The objective is to showcase engineering decision-making, maintainable automation implementation, and clear documentation of automated testing outcomes.
