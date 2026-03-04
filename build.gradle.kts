plugins {
    java
    groovy
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // PostgreSQL driver
    runtimeOnly("org.postgresql:postgresql")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing - JUnit 5 + Spring Boot Test (includes Mockito, AssertJ)
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Testing - H2 in-memory DB for Spring context tests
    testRuntimeOnly("com.h2database:h2")

    // Testing - Spock Framework (Groovy 4.x)
    testImplementation("org.apache.groovy:groovy:4.0.23")
    testImplementation("org.spockframework:spock-core:2.4-groovy-4.0")
    testImplementation("org.spockframework:spock-spring:2.4-groovy-4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
