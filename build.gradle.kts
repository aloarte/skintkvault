val exposedVersion: String by project
val h2Version: String by project
val logbackVersion: String by project
val mockkVersion: String by project
val koinVersion: String by project
val koinKtorVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project

plugins {
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
}

group = "com.skintker"
version = "0.0.1"
application {
    mainClass.set("com.skintker.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {

    //Kotlin
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    //Ktor dependencies
    implementation("io.ktor:ktor-server-metrics-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    //Koin
    implementation ("io.insert-koin:koin-ktor:$koinKtorVersion")
    implementation ("io.insert-koin:koin-logger-slf4j:$koinKtorVersion")
    implementation ("io.insert-koin:koin-core:$koinVersion")
    testImplementation ("io.insert-koin:koin-test:$koinVersion")
    testImplementation ("io.insert-koin:koin-test-junit4:$koinVersion")
    testImplementation ("io.insert-koin:koin-test-junit5:$koinVersion")

    //Expresso + H2 database
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")

    //Firebase
    implementation ("com.google.firebase:firebase-admin:9.1.1")

    //Testing dependencies
    testImplementation ("io.mockk:mockk:$mockkVersion")



}