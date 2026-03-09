# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Purpose

This is a **template / source branch** for new Spring Boot projects. The `My*` placeholder classes (`MyEntity`, `MyRepository`, `MyService`, `MyController`) are intentionally minimal stubs meant to be replaced with real domain logic when forking.

## Commands

```bash
# Start PostgreSQL (required for bootRun)
docker compose up -d

# Run the application
./gradlew bootRun

# Run all tests (JUnit + Spock — no Docker needed)
./gradlew test

# Run a single test class
./gradlew test --tests "com.example.sandbox.service.MyServiceTest"

# Run a single Spock spec
./gradlew test --tests "com.example.sandbox.service.MyServiceSpec"

# Compile without running tests
./gradlew compileJava

# Build JAR
./gradlew build
```

## Architecture

**Package root:** `com.example.sandbox`

The project follows a standard layered architecture:

```
controller → service → repository → entity
```

- **Controllers** are `@RestController` classes under `controller/`, mapped with `@RequestMapping`.
- **Services** are `@Service` classes injected via Lombok `@RequiredArgsConstructor` (constructor injection, no `@Autowired`).
- **Repositories** extend `JpaRepository<Entity, Long>` — no boilerplate needed for standard CRUD.
- **Entities** use Lombok `@Data @Builder @NoArgsConstructor @AllArgsConstructor` and Jakarta Persistence annotations.
- **DTOs** (when needed) live in `dto/` as Java records.
- **Enums** live in `enums/`.
- **Config beans** (`RestTemplate`, etc.) live in `config/`.

## Testing Conventions

Two test styles coexist — both run on `./gradlew test` via the JUnit Platform:

**JUnit 5 + Mockito** (`src/test/java`):
- `@ExtendWith(MockitoExtension.class)` — no Spring context loaded
- Group tests with `@Nested` + `@DisplayName`
- Use AssertJ (`assertThat`) for assertions, `verify()` to check interactions
- Use `verifyNoMoreInteractions(repository)` to catch unexpected calls

**Spock** (`src/test/groovy`):
- Specs extend `Specification`, use `@Subject` to mark the class under test
- Mocks declared as `MyRepository repository = Mock()`
- `given / when / then` blocks; use `where:` tables for data-driven cases

**Test profile:** `@ActiveProfiles("test")` activates `src/test/resources/application-test.yml`, which swaps PostgreSQL for H2 in PostgreSQL-compatibility mode (`MODE=PostgreSQL`).

## Key Configuration

- **DB (runtime):** PostgreSQL 16 via Docker Compose — `sandbox_db`, user `sandbox_user`, password `sandbox_password`, port `5432`
- **DB (test):** H2 in-memory, `ddl-auto: create-drop`
- **JPA:** `ddl-auto: update` in dev (fine for templates; use Flyway in production), `show-sql: true`
- **Actuator:** `/actuator/health` is available by default (only `health` and `info` exposed over HTTP)
- **DevTools:** enabled for hot reload during `bootRun`

## Java Coding Rules

### Principles
- **KISS** — prefer the simplest solution that works. Avoid clever abstractions until there is a clear, repeated need for them.
- **SOLID** — one responsibility per class, depend on interfaces not implementations, favour composition over inheritance.
- **Immutability first** — declare fields `final`, use records for DTOs, return unmodifiable collections where possible.

### Style
- **Streams over loops** — use the Stream API for any collection transformation, filtering, or aggregation. Avoid `for`/`while` loops unless mutating index or managing stateful iteration that streams cannot express cleanly.
- **`var` for locals** — use `var` for local variables where the type is obvious from the right-hand side.
- **Optional** — return `Optional<T>` from methods that may produce no result; never return `null` from a public method.
- **Early returns** — guard-clause at the top of a method rather than deep nesting.
- **Method length** — if a method exceeds ~15 lines, consider extracting a private helper with a descriptive name.

### Examples
```java
// Preferred — stream pipeline
int total = events.stream()
        .map(e -> resolve(e.getType()).getPoints())
        .mapToInt(Integer::intValue)
        .sum();

// Avoid — imperative loop
int total = 0;
for (Event e : events) {
    total += resolve(e.getType()).getPoints();
}
```

```java
// Preferred — guard clause
if (user.isEmpty()) return ResponseEntity.notFound().build();
return ResponseEntity.ok(user.get());

// Avoid — nested if/else
if (user.isPresent()) {
    return ResponseEntity.ok(user.get());
} else {
    return ResponseEntity.notFound().build();
}
```

### Spring-specific
- Services must not call other services' repositories directly — always go through the owning service.
- Controllers are thin: no business logic, only delegation to the service layer and HTTP response mapping.
- Use `@Transactional` only on service methods that write; do not annotate read-only methods unless you need explicit isolation.

## Build

- Gradle Kotlin DSL (`build.gradle.kts`)
- Java 21 toolchain — use `languageVersion.set(JavaLanguageVersion.of(21))` syntax (not `=` assignment) when adding Groovy toolchain config
- Both `java` and `groovy` plugins are applied so Spock specs compile alongside Java sources
