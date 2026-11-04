# Test plan

## Scope and objectives

Validate room inventory, reservation contracts, guest validation, authorization and shared room data across HTTP and browser layers. Demonstrate maintainable API/UI separation and resource ownership. Payment, load, penetration testing, email delivery and cross-browser certification are excluded.

## Testing types and environment

Smoke checks room retrieval/rendering. API regression covers schema, CRUD, conflicts and invalid payloads. UI regression covers navigation/forms and invalid contacts. Integration tests verify API data reflected in the browser for one generated ID and UI bookings persisted through the API. Java 17 tests, Maven 3.9+, Chrome/Selenium Manager and serial TestNG. Direct Maven commands default to the public target with writes disabled; the supplied local runner starts the pinned application with its separate Java 26/Node 24 runtimes for full isolated coverage.

## Entry and exit criteria

Entry: dependencies resolve, sources compile, Chrome is installed, URLs address the same application, and writes are authorized on a disposable target with cleanup credentials. Exit: selected scenarios execute, unexpected failures are resolved or recorded, report evidence exists and owned data is removed. Skips do not satisfy authenticated coverage; compilation alone does not validate business workflows.

## Test data strategy

UUID-named rooms isolate writes. Bookings use future dates in owned rooms and omit optional contacts to avoid notifications. Try-with-resources deletes tracked bookings before their room; cleanup failures propagate. No shared mutable test data, seed deletion, order dependencies, sleeps or retries. Public UI validation submits invalid data only. Keep public volume small.

## Risks, assumptions and limits

Public resets may remove owned data mid-run. Deployed routing and upstream source may differ; the selected contract must match the deployment. The official local rewrites expose backend responses on dynamic paths. The home page limits rooms to three, so generated rooms are inspected at their ID-specific reservation pages. The 500 characterization cases document defects without claiming desirable behavior. A create/discovery transport failure can require marker-based manual cleanup. Browser downloads, network restrictions and missing runtimes can block setup. Credentials are environment-only; screenshots must be reviewed before sharing. Valid UI booking creates a notification; cleanup discovers only messages with the test's unique guest name and deletes them, then removes the booking and room. Local resets are disabled and processes are stopped in finally.
