# Implemented scenario matrix

| ID | Layer | Scenario | Priority | Expected Result |
|---|---|---|---|---|
| API-01 | API | Room schema and individual consistency | P0 | 200; valid array; selected detail equals list item |
| API-02 | API negative | Room creation without token | P1 | 401; no creation |
| API-03 | API characterization | Unknown room identifier | P2 | Current proxy 500; known error-mapping defect |
| API-04 | API write | Room create/read/update/delete | P0 | Unique ID discovered; update persists; cleanup succeeds |
| API-05 | API negative | Missing booking fields | P1 | 400 with errors |
| API-06 | API characterization | Malformed JSON | P2 | Current proxy 500; known error-mapping defect |
| API-07 | API negative | Booking read without authentication | P1 | Rewrites contract 403 (handler-only proxy contract 401); unexpected 500 fails visibly |
| API-08 | API write | Booking CRUD and conflicting dates | P0 | 201, persisted data, 409 duplicate, update; cleanup deletes booking and room |
| UI-01 | UI | Home room cards and booking links | P0 | Rooms and navigation render |
| UI-02 | UI negative | Guest form and required validation | P1 | Form visible; firstname/lastname errors |
| UI-03 | UI negative | Invalid email and short phone | P1 | Email/phone errors |
| HYB-01 | Integration write | API-created and updated room reflected in its reservation UI | P0 | Generated ID, exact description, heading and guest form; revised description after update |
| HYB-02 | Integration write | API room creation → valid UI booking/confirmation → API verification | P0 | Exactly one matching booking; generated ID, guest, room and dates match; notification/booking/room cleaned |

All belong to regression; API-01/UI-01 are smoke. API-04/API-08/HYB-01/HYB-02 require explicit writes and credentials. Tests have no execution dependencies. The local runner configures a disposable instance and documented local credentials automatically. See EXECUTION_RECORD for verified execution outcomes.
