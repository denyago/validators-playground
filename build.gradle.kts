import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    id("org.jlleitschuh.gradle.ktlint") version "12.0.2"
}

group = "name.denyago"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")

    // Jakarta Validation
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // Tribune and ArrowKT
    implementation("com.sksamuel.tribune:tribune-core:1.3.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.0")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
