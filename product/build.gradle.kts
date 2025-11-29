plugins {
    java
    id("org.springframework.boot") version "3.5.8"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
}

tasks.withType<Test> {
    useJUnitPlatform()
}
