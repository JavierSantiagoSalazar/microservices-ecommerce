plugins {
    java
    id("io.spring.dependency-management") version "1.1.7" apply false
}

group = "com.link"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

subprojects {

    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencies {

        //Security
        implementation("org.springframework.boot:spring-boot-starter-security")

        //OpenAPI
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.14")

        //Web
        implementation("org.springframework.boot:spring-boot-starter-web")

        // BOM Spring Boot compartido
        implementation(platform("org.springframework.boot:spring-boot-dependencies:3.5.8"))

        // JPA
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")

        // Lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        // MySQL Connector
        runtimeOnly("com.mysql:mysql-connector-j")

        //MapStruct
        implementation("org.mapstruct:mapstruct:1.6.3")
        annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

        // JSON:API - Katharsis
        implementation("io.katharsis:katharsis-spring:3.0.2")

        // WebFlux
        implementation("org.springframework.boot:spring-boot-starter-webflux")

        // Resilience4j
        implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
        implementation("io.github.resilience4j:resilience4j-reactor:2.2.0")

        // Testing
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

        testImplementation("org.mockito:mockito-core:5.12.0")

        testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")

        testImplementation("org.springframework.security:spring-security-test")

        testImplementation("com.h2database:h2")

    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
