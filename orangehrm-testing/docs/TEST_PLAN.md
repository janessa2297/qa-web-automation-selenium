# OrangeHRM Test Plan

## Objective and risk
Validate critical authentication and PIM navigation paths. Preserve a clean separation between read-only tests and state-changing E2E tests, especially on the shared public OrangeHRM demo.

## Suites

| ID | Scenario | Automation class | Default suite | Expected result |
|---|---|---|---|---|
| AUTH-001 | Login page controls | `LoginTests.loginPageRendersRequiredControls` | Yes | Title, username, password, login button visible |
| AUTH-002 | Valid admin login | `LoginTests.validLoginOpensDashboard` | Yes | Navigates to dashboard |
| AUTH-003 | Invalid logins (data driven) | `LoginTests.invalidCredentialsAreRejected` | Yes | Invalid credentials message |
| AUTH-004 | Empty fields | `LoginTests.emptyCredentialsShowRequiredValidation` | Yes | Both fields show Required validation |
| AUTH-005 | Missing username | `LoginTests.missingUsernameShowsValidation` | Yes | Only username shows Required |
| AUTH-006 | Missing password | `LoginTests.missingPasswordShowsValidation` | Yes | Only password shows Required |
| AUTH-007 | Password masking | `LoginTests.passwordInputIsMasked` | Yes | Input type is password |
| AUTH-008 | Correct rejected credentials | `LoginTests.validLoginWorksAfterRejectedAttempt` | Yes | Valid retry opens dashboard without reload |
| SESSION-001 | Direct access without login (two routes) | `SessionTests.protectedPagesRequireAuthentication` | Yes | Dashboard and PIM redirect to login |
| SESSION-002 | Protected route after logout | `SessionTests.loggedOutSessionCannotReopenDashboard` | Yes | Previous session cannot reopen dashboard |
| SESSION-003 | Refresh authenticated page | `SessionTests.dashboardRemainsAvailableAfterRefresh` | Yes | Dashboard remains available |
| PIM-002 | Unknown employee ID | `EmployeeSearchTests.unknownEmployeeIdShowsNoRecords` | Yes | No Records Found |
| PIM-003 | Reset employee search | `EmployeeSearchTests.resetRestoresEmployeeList` | Yes | Filter cleared and demo rows restored |
| PIM-004 | Required employee names | `EmployeeSearchTests.employeeNameIsRequired` | Yes | Both name fields show Required; form stays open |
| NAV-001 | Open PIM employee list | `DashboardTests.employeeListCanBeOpened` | Yes | Employee search visible |
| NAV-002 | Logout | `DashboardTests.logoutReturnsToLogin` | Yes | Login screen visible after logout |
| PIM-001 | Add → search → delete test-owned employee | `EmployeeLifecycleWriteTests.newEmployeeCanBeCreatedSearchedAndCleanedUp` | **No (explicit opt-in)** | Own new employee found, cleaned up |

## Planned expansion

- PIM edit employee details and verify persistence
- Positive employee search using a known, controlled fixture
- Role-based scenarios on a controlled environment
- Cross-browser validation and runtime configuration matrix
- CI with uploaded reports / screenshot artifacts

## Entry criteria
- JDK, Maven, supported browser installed
- Test environment reachable
- Demo credentials still valid
- No secrets in repository

## Exit criteria
- Read-only suite tests pass locally on a chosen browser
- Failures investigated and screenshots attached to reports
- Test evidence uploaded only after sanitization
- Any state-mutating tests have explicit permission and cleanup

## Known risks

The default suite has 18 test invocations. Employee search reset assumes the public demo contains at least one employee. Empty-name submission exercises client validation and does not intentionally create an employee. Password masking checks the HTML input type, not transport or storage security. Session checks verify UI redirection, not comprehensive server authorization.
Public demo data resets, concurrent user edits, changing locators, intermittent access. These are environment risks, not reasons to conceal flaky test results.
