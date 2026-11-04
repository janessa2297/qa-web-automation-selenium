# API contracts and research

Inspected 2026-11-02 at official upstream commit `efe2deb8ce02db541adbf9d3a6604caf5fbdaae6`.

The Next.js frontend routes Spring room, booking, auth, branding, report and message services. Tests target **`https://automationintesting.online/api`**, locally `http://127.0.0.1:3003/api`. Backend paths/status codes differ. The initial review below described handler implementations; actual local execution also verified the routing precedence described next.

## Verified local routing contract

The official [next.config.js](https://github.com/mwinteringham/restful-booker-platform/blob/efe2deb8ce02db541adbf9d3a6604caf5fbdaae6/assets/next.config.js) returns array rewrites. These run before dynamic routes, as documented in [Next.js rewrite ordering](https://nextjs.org/docs/app/api-reference/config/next-config-js/rewrites). Thus static routes execute their handlers, but dynamic API paths are forwarded directly to Spring. `api.contract=rewrites` is the default and the local runner sets it explicitly. No response-driven fallback is used.

| Operation | Verified rewrites response | Handler-only `proxy` contract |
|---|---|---|
| Room POST | 200 `{success:true}` | Same |
| Room PUT | 202 room object | 200 `{success:true}` |
| Room DELETE | 202 empty body | 200 `{success:true}` |
| Booking POST | 201 booking object | Same |
| Booking GET by ID without token | 403 | 401 |
| Booking PUT | 200 `{bookingid,booking}` | 200 `{success:true}` |
| Booking DELETE | 202 empty body | 200 `{success:true}` |
| Notification DELETE | 202 empty body | 200 `{success:true}` |

Booking list by room ID remains the static authenticated handler, returning 200 `{bookings:[...]}`. Auth/login remains the static handler returning `{token}`. Missing numeric room lookup returns 500 in the current pinned application; malformed booking JSON also returns 500. Both are characterized defects rather than desired behavior. The optional handler-only contract is source-supported but is not claimed to be the routing of the pinned local deployment.

## Handler implementations

Primary sources: [proxy route implementations](https://github.com/mwinteringham/restful-booker-platform/tree/efe2deb8ce02db541adbf9d3a6604caf5fbdaae6/assets/src/app/api), [room controller/service/model](https://github.com/mwinteringham/restful-booker-platform/tree/efe2deb8ce02db541adbf9d3a6604caf5fbdaae6/room/src/main/java/com/automationintesting), [booking controller/service/model](https://github.com/mwinteringham/restful-booker-platform/tree/efe2deb8ce02db541adbf9d3a6604caf5fbdaae6/booking/src/main/java/com/automationintesting). Service READMEs describe local Swagger, e.g. `http://localhost:3000/booking/swagger-ui/index.html`; live Swagger access is not claimed.

| Method | Proxy path | Authentication | Source-verified proxy response |
|---|---|---|---|
| POST | `/auth/login` | username/password JSON | 200 `{token}`; 401 invalid credentials; token is JSON, not a proxy cookie |
| GET | `/room` | None | 200 `{rooms:[...]}`; paired `checkin`, `checkout` optionally filter availability |
| GET | `/room/{id}` | None | 200 room; empty upstream 404 can map to 500 `[]` during JSON parsing |
| POST | `/room` | `token` cookie | 200 `{success:true}`; 401 missing token; 400 invalid fields |
| PUT | `/room/{id}` | `token` cookie | 200 `{success:true}`; 401 missing token |
| DELETE | `/room/{id}` | `token` cookie | 200 `{success:true}`; 401 missing token |
| POST | `/booking` | None | 201 booking object unwrapped from `data.booking`; 400 validation; 409 date conflict; malformed JSON maps to 500 |
| GET | `/booking?roomid={id}` | `token` cookie | 200 `{bookings:[...]}`; 401 missing token; 400 missing roomid |
| GET | `/booking/{id}` | `token` cookie | 200 booking; 401 missing token; upstream errors forwarded where parseable |
| PUT | `/booking/{id}` | `token` cookie | 200 `{success:true}`; 401 missing token; 409 date conflict |
| DELETE | `/booking/{id}` | `token` cookie | 200 `{success:true}`; 401 missing token |
| GET | `/message` | None in proxy | 200 `{messages:[{id,name,subject,read}]}`; used only to identify test-owned notifications |
| DELETE | `/message/{id}` | `token` cookie | 200 `{success:true}`; 401 missing token |

No PATCH route was verified or implemented. Backing services use `/room/`, `/booking/` with 201 creation, 202 deletion and cookie authentication; those are not proxy contracts.

Room payload fields: `roomName`, `type` (`Single|Double|Twin|Family|Suite`), `accessible`, `image`, `description`, `features` string array, `roomPrice` (1–999). Creation ID is discovered by a unique room name in listing.

Booking payload fields: `roomid`, `firstname` (3–18), `lastname` (3–30), `depositpaid`, `bookingdates:{checkin,checkout}` using ISO dates. Optional `email` and `phone` can trigger notification messages, so valid API test data omits them. All credentials are injected through environment variables; tokens are request-local cookies.

Initial public read-only probes: `/room` returned 200 with three rooms; `/room/2147483647` and `/booking/1` returned 500. The latter matched neither the handler-only no-token 401 nor the verified rewrites no-token 403. Later local execution successfully validated authenticated CRUD and both hybrid workflows against the pinned real application; see EXECUTION_RECORD for actual results and preserved public-deployment evidence.

UI source: [HotelRoomInfo.tsx](https://github.com/mwinteringham/restful-booker-platform/blob/efe2deb8ce02db541adbf9d3a6604caf5fbdaae6/assets/src/components/home/HotelRoomInfo.tsx) renders `.room-card`, `p.card-text`, `/reservation/{roomid}` links, but [Availability.tsx](https://github.com/mwinteringham/restful-booker-platform/blob/efe2deb8ce02db541adbf9d3a6604caf5fbdaae6/assets/src/components/home/Availability.tsx) uses `rooms.slice(0,3)`. Newly created rooms are genuinely visible at `/reservation/{id}` through RoomDetails, not on an additional home card. [BookingForm.tsx](https://github.com/mwinteringham/restful-booker-platform/blob/efe2deb8ce02db541adbf9d3a6604caf5fbdaae6/assets/src/components/reservation/BookingForm.tsx) renders `#doReservation`, named guest inputs, `[role=alert]`, and `Booking Confirmed`. Query dates initialize selection. No invented test IDs or credentials are used.
