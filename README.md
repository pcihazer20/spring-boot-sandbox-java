# Spring Boot Sandbox — Java

A ready-to-fork template project for building Spring Boot applications on the JVM. Clone it, rename the package, and start building — everything you need to get a production-style backend off the ground is already wired up.

## Purpose

This project is intended as a **source branch** for spinning off new Spring Boot projects. It provides a well-structured, working baseline with sensible defaults for the stack listed below. Rather than starting from a blank Spring Initializr export every time, fork this repo and adapt it to your needs.

All domain classes (`MyEntity`, `MyRepository`, `MyService`, `MyController`) are intentional placeholders. Replace them with your own domain model when you fork.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.4 |
| Build | Gradle 8.10 (Kotlin DSL) |
| Persistence | Spring Data JPA + PostgreSQL 16 |
| Test DB | H2 (in-memory, PostgreSQL-compatible mode) |
| Boilerplate reduction | Lombok |
| Unit testing | JUnit 5 + Mockito + AssertJ |
| BDD testing | Spock 2.4 (Groovy 4) |
| Local infrastructure | Docker Compose |

## Project Structure

```
src/
├── main/
│   ├── java/com/example/sandbox/
│   │   ├── Application.java              # Entry point
│   │   ├── controller/
│   │   │   └── MyController.java         # Placeholder REST controller
│   │   ├── service/
│   │   │   └── MyService.java            # Placeholder service
│   │   ├── repository/
│   │   │   └── MyRepository.java         # Placeholder Spring Data JPA repository
│   │   └── entity/
│   │       └── MyEntity.java             # Placeholder JPA entity
│   └── resources/
│       └── application.yml               # Main config (PostgreSQL)
└── test/
    ├── java/com/example/sandbox/
    │   ├── ApplicationTests.java          # Spring context smoke test
    │   └── service/
    │       └── MyServiceTest.java         # JUnit 5 + Mockito unit tests (placeholder)
    ├── groovy/com/example/sandbox/
    │   └── service/
    │       └── MyServiceSpec.groovy       # Spock BDD spec (placeholder)
    └── resources/
        └── application-test.yml          # Test config (H2 in-memory)
```

## Prerequisites

- Java 21+
- Docker (for the local PostgreSQL instance)

## Getting Started

### 1. Start the database

```bash
docker compose up -d
```

This starts a PostgreSQL 16 container on port `5432` with:

| Setting | Value |
|---|---|
| Database | `sandbox_db` |
| Username | `sandbox_user` |
| Password | `sandbox_password` |

### 2. Run the application

```bash
./gradlew bootRun
```

The server starts on `http://localhost:8080`.

### 3. Run the tests

```bash
./gradlew test
```

Tests run against H2 in-memory (no Docker needed). Both JUnit and Spock specs are picked up automatically.

### 4. Build a JAR

```bash
./gradlew build
java -jar build/libs/spring-boot-sandbox-java-0.0.1-SNAPSHOT.jar
```

## Testing Strategy

Two complementary test styles are set up side-by-side:

### JUnit 5 + Mockito (`src/test/java`)

Standard unit tests using `@ExtendWith(MockitoExtension.class)`. Tests are grouped with `@Nested` classes and `@DisplayName` annotations for readable output. Repository interactions are verified with AssertJ assertions and Mockito `verify()`.

### Spock (`src/test/groovy`)

BDD-style specs using Groovy's `given / when / then` blocks. Useful for describing behaviour in plain English and for data-driven testing with `where:` tables. Both suites run on the same `./gradlew test` invocation via the JUnit Platform.

## Forking This Project

To use this as a base for your own project:

1. Fork or clone the repository.
2. Rename the root project in `settings.gradle.kts`.
3. Update `group` in `build.gradle.kts` to your own package namespace.
4. Rename the Java package from `com.example.sandbox` to your own (find-and-replace across `src/`).
5. Replace the `My*` placeholder classes (`MyEntity`, `MyRepository`, `MyService`, `MyController`) with your own domain model.
6. Update `application.yml` with your database name and credentials (or inject them via environment variables).
7. Adapt the test stubs in `MyServiceTest.java` and `MyServiceSpec.groovy` to cover your new domain logic.

## Configuration Reference

### `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/sandbox_db
    username: sandbox_user
    password: sandbox_password
  jpa:
    hibernate:
      ddl-auto: update   # change to 'validate' or use Flyway in production
    show-sql: true

server:
  port: 8080
```

### `src/test/resources/application-test.yml`

Tests activate the `test` profile automatically via `@ActiveProfiles("test")` and use H2 in PostgreSQL-compatibility mode — no running database required.

## License

This project is released as a public-domain template. Use it freely as the foundation for your own projects.