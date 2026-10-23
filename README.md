# api-automation-framework-java

![API Tests](https://github.com/YOUR_GITHUB_USERNAME/api-automation-framework-java/actions/workflows/api-tests.yml/badge.svg)

API test framework in Java, tested against [restful-booker](https://restful-booker.herokuapp.com/apidoc/index.html)
(a public practice API).

I built this to show the same approach I use for backend regression at work, but on a public API, so there's no
company code in here.

**Stack:** Java 17, REST Assured, JUnit 5, Cucumber, H2/JDBC, Allure, Maven, GitHub Actions / Azure Pipelines

## What's covered

- Booking CRUD, with read-back checks after every write
- Auth: valid/invalid credentials, and missing or tampered tokens
- Negative cases: unknown ids, missing fields, wrong types
- JSON schema checks on responses
- Data-driven tests from a database table (`GUESTS`)
- A few BDD scenarios in Cucumber
- Response time checks
- Date handling: year-end and leap-day stays

Every booking a test creates is logged in a `CREATED_BOOKINGS` table and deleted at the end of the run.

## Layout

```
config/    ConfigManager - reads -D props, env vars, then config.properties
client/    BaseApiClient + one client per resource (Auth, Booking)
model/     request/response records
data/      BookingFactory (random test data), GuestRecord
db/        JDBC connection + queries
support/   end-of-run cleanup listener
tests/     JUnit tests
cucumber/  runner, steps, hooks
```

## Running

```bash
mvn test                                   # everything
mvn test -Dgroups=smoke                    # smoke only
mvn test -Dcucumber.filter.tags="@smoke"   # BDD smoke only
mvn test -Dbase.url=https://other-env      # different environment
mvn test -Dgroups=known-defect -Dexcluded.groups=none   # known bugs (these fail)
mvn allure:serve                           # open the report
```

## Bugs found

The API has a few problems (500 instead of 400 on bad input, 201 on DELETE, etc.). I've listed them in
[KNOWN_ISSUES.md](KNOWN_ISSUES.md), with how each one is handled in the tests.

## To do

- WireMock stubs so PR builds don't depend on the live API
- Testcontainers Postgres instead of H2
- Generate contract tests from an OpenAPI spec
