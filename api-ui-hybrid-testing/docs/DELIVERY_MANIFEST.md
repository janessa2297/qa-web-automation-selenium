# Delivery manifest

README.md and .gitignore were updated from the starter. All other implementation files listed below were added. Existing .gitkeep files were retained. This workspace has no Git checkout or sibling SauceDemo/OrangeHRM projects; neither existing project was modified.

Generated reports, screenshots, logs, downloaded runtimes and the official application archive/build are ignored under target/ or .tools/. No nested repository, commit or push was created.

The final implementation contains 19 Java files and 13 verified local scenarios. Run scripts/Run-LocalRegression.ps1 for isolated regression. The CI template must be installed in the actual parent checkout with scripts/Install-MonorepoWorkflow.ps1.

## Files

- `.gitignore`
- `ci/api-ui-hybrid-tests.yml`
- `docs/API_ENDPOINTS.md`
- `docs/DELIVERY_MANIFEST.md`
- `docs/EXECUTION_RECORD.md`
- `docs/FRAMEWORK_ARCHITECTURE.md`
- `docs/TEST_CASES.md`
- `docs/TEST_PLAN.md`
- `pom.xml`
- `README.md`
- `scripts/Install-MonorepoWorkflow.ps1`
- `scripts/Run-LocalRegression.ps1`
- `src/main/java/com/portfolio/hybrid/api/ApiClient.java`
- `src/main/java/com/portfolio/hybrid/api/ApiContract.java`
- `src/main/java/com/portfolio/hybrid/api/BookingApiClient.java`
- `src/main/java/com/portfolio/hybrid/api/RoomApiClient.java`
- `src/main/java/com/portfolio/hybrid/config/FrameworkConfig.java`
- `src/main/java/com/portfolio/hybrid/driver/DriverFactory.java`
- `src/main/java/com/portfolio/hybrid/models/Booking.java`
- `src/main/java/com/portfolio/hybrid/models/Room.java`
- `src/main/java/com/portfolio/hybrid/pages/BasePage.java`
- `src/main/java/com/portfolio/hybrid/pages/HomePage.java`
- `src/main/java/com/portfolio/hybrid/pages/ReservationPage.java`
- `src/main/resources/config.properties`
- `src/test/java/com/portfolio/hybrid/api/BookingApiTest.java`
- `src/test/java/com/portfolio/hybrid/api/RoomApiTest.java`
- `src/test/java/com/portfolio/hybrid/base/BaseUiTest.java`
- `src/test/java/com/portfolio/hybrid/base/OwnedResources.java`
- `src/test/java/com/portfolio/hybrid/data/TestData.java`
- `src/test/java/com/portfolio/hybrid/integration/RoomHybridTest.java`
- `src/test/java/com/portfolio/hybrid/reporting/ExtentListener.java`
- `src/test/java/com/portfolio/hybrid/ui/BookingUiTest.java`
- `src/test/resources/testdata/rooms.schema.json`
- `testng.xml`
