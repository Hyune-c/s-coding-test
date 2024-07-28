import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"

    val kotlinVersion = "1.9.24"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false

    kotlin("kapt") version kotlinVersion apply false
    kotlin("plugin.jpa") version kotlinVersion apply false
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    group = "com.hyunec"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.kapt")

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "kotlinx-serialization")

    val springCloudVersion: String by project

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")


        val kotlinxSerializationVersion: String by project
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${kotlinxSerializationVersion}")

        // kotest
        val kotestVersion: String by project
        val kotestExtVersion: String by project
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.kotest:kotest-property:$kotestVersion")
        testImplementation("io.kotest:kotest-extensions-now:${kotestVersion}")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestExtVersion")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

        val datafakerVersion: String by project
        testImplementation("net.datafaker:datafaker:$datafakerVersion")

        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        testImplementation("io.projectreactor:reactor-test")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
