
val exposedVersion: String by project
val h2Version: String by project
val postgresVersion: String by project
val logbackVersion: String by project
val mockkVersion: String by project
val koinVersion: String by project
val koinKtorVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val spekVersion: String by project

plugins {
    jacoco
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    id("org.unbroken-dome.test-sets") version "4.0.0"
    id("io.gitlab.arturbosch.detekt") version("1.22.0")
    id("com.github.johnrengelman.shadow") version("6.0.0")
}

group = "com.skintker"
version = "1.0.0"
val mainClassName = "com.skintker.Application"

application {
    mainClass.set("com.skintker.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

ktor {
    fatJar {
        archiveFileName.set("skintkvault-$version.jar")
    }
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
    implementation("io.ktor:ktor-server-freemarker:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
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
    implementation("org.postgresql:postgresql:$postgresVersion")

    implementation("com.h2database:h2:$h2Version")

    //Logs
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("ch.qos.logback:logback-classic:1.3.5")

    //Firebase
    implementation ("com.google.firebase:firebase-admin:9.1.1")

    implementation("org.apache.commons:commons-math3:3.6.1")

    //Testing dependencies
    testImplementation ("io.mockk:mockk:$mockkVersion")
    testImplementation("io.ktor:ktor-server-host-common:$ktorVersion")
    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.ktor:ktor-client-cio:$ktorVersion")

    //JaCoCo
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    implementation("org.jacoco:org.jacoco.core:0.8.7")

}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to mainClassName)
    }
}

tasks.create("stage") {
    dependsOn(tasks.getByName("build"),tasks.getByName("clean"))
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
}