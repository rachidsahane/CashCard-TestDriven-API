Updated `README.md` to remove instructions to run the application, instruct running the test suite via `./gradlew test`, and keep example `curl` requests for manual checks.

```markdown
# CashCard (TDD Example)

A simple CashCard REST API implemented with Spring Boot that demonstrates Test-Driven Development (TDD).  
This repository is focused on tests that define and verify behavior across multiple scenarios: CRUD operations, paging/sorting, authentication and authorization, and error handling.

## Key ideas
- Emphasis on TDD: tests define expected behavior (see `src/test/java/com/example/cashcard`).
- Lightweight REST API for managing cash cards.
- Tests cover positive and negative scenarios (found / not found, auth failures, ownership restrictions, paging and sorting).

## Technologies
- Java (JDK 11+ recommended)
- Spring Boot
- Gradle (wrapper included)
- JUnit 5
- JsonPath for response assertions
- In-memory database during tests (test configuration)

## Project structure (important paths)
- Application tests: `src/test/java/com/example/cashcard/CashcardApplicationTests.java`
- Main sources: `src/main/java/...`
- Gradle wrapper: `./gradlew` (UNIX-like systems) — tests are runnable directly from the terminal

## Prerequisites
- JDK 11 or newer
- On UNIX-like systems use `./gradlew`
- IntelliJ IDEA (optional) — tests run from the IDE as well

## Run tests (terminal)
Run the full test suite (recommended; tests are the primary verification method for this project):
- `./gradlew test`

Notes about tests:
- Tests are Spring Boot integration tests and may start embedded contexts on random ports.
- Tests use seeded/test data and isolate state where required; they exercise authentication, authorization, paging, sorting, and error flows.

## Authentication (used by tests)
The tests use HTTP Basic authentication. Example users used in tests:
- `sarah1` / `abc123` (owner of some cards)
- `hank1` / `qrs456`
- `kumar2` / `xyz789`

## API (summary)
- GET `/cashcards`
  - List all cash cards. Supports paging and sorting query parameters: `page`, `size`, `sort`.
- GET `/cashcards/{id}`
  - Retrieve a single cash card by id (returns `200`, `404`, or `403` depending on ownership and existence).
- POST `/cashcards`
  - Create a new cash card (returns `201` with `Location` header).
- PUT `/cashcards/{id}`
  - Update an existing cash card (returns `204` or `404`).
- DELETE `/cashcards/{id}`
  - Delete a cash card (returns `204` or `404`).
    
## Tests — what they demonstrate
The test class `CashcardApplicationTests` validates:
- Retrieval of existing and non-existing resources (`200`, `404`)
- Creation and retrieval of new resources (`201` -> follow `Location`)
- Update and delete flows (`204`, `404`)
- Paging and sorting behavior
- Authentication: unauthorized users receive `401`
- Authorization: users cannot access cards they do not own (`403` or `404` as designed)
- Context isolation for stateful tests using `@DirtiesContext` where needed

## Notes
- This repository is an educational example focusing on writing clear, descriptive tests before or alongside implementation.
- Use `./gradlew test` as the primary way to run and verify behavior. Adapt authentication, data seeding, and DB settings in `application.yml` / `application-test.yml` if needed.
```