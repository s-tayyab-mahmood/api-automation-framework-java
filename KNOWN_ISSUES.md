# Known issues

Bugs in restful-booker that I found while writing these tests. The tests assert what the API does now,
so if it gets fixed the test will fail and can be updated.

| # | Endpoint | Actual | Expected | Handled in |
|---|----------|--------|----------|------------|
| 1 | `POST /booking` with missing fields | 500 | 400 | `BookingNegativeTest` (strict version is `@Disabled`) |
| 2 | `DELETE /booking/{id}` | 201 | 200 / 204 | `BookingCrudTest` |
| 3 | `GET /ping` | 201 | 200 | `HealthCheckTest` |
| 4 | `POST /auth` with bad credentials | 200 + `"reason": "Bad credentials"` | 401 | `AuthTest` checks the body |
| 5 | `DELETE` on an already-deleted booking | 405 | 404 | cleanup treats 405 as done |
