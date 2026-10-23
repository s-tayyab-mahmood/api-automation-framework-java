# Known issues

Bugs in restful-booker that I found while writing these tests. The tests assert what the API does now,
so if it gets fixed the test will fail and can be updated.

The expected behaviour for each one is in `KnownDefectsTest` (tag `known-defect`, skipped by default). Last confirmed 23 Oct 2026: all three still fail.

| # | Endpoint | Actual | Expected | Handled in |
|---|----------|--------|----------|------------|
| 1 | `POST /booking` with missing fields | 500 | 400 | `BookingNegativeTest` / `KnownDefectsTest` |
| 2 | `DELETE /booking/{id}` | 201 | 200 / 204 | `BookingCrudTest` |
| 3 | `GET /ping` | 201 | 200 | `HealthCheckTest` |
| 4 | `POST /auth` with bad credentials | 200 + `"reason": "Bad credentials"` | 401 | `AuthTest` checks the body |
| 5 | `DELETE` on an already-deleted booking | 405 | 404 | cleanup treats 405 as done |
| 6 | `POST /booking` with checkout before checkin | 200, booking saved | 400 | `KnownDefectsTest` |
| 7 | `POST /booking` with an invalid date string | 200, booking saved | 400 | `KnownDefectsTest` |
| 8 | Any request with an `Accept` header other than exactly `application/json` (e.g. REST Assured's default list) | 418 I'm a teapot | 406, or accept a list that includes JSON | `BaseApiClient` sends `Accept: application/json` |
