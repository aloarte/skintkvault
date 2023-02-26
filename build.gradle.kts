
val exposedVersion: String by project
val h2Version: String by project
val logbackVersion: String by project
val mockkVersion: String by project
val koinVersion: String by project
val koinKtorVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val spekVersion: String by project

plugins {
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
    id("org.unbroken-dome.test-sets") version "4.0.0"
    id("io.gitlab.arturbosch.detekt") version("1.22.0")
    id("com.github.johnrengelman.shadow") version("6.0.0")
}

group = "com.skintker"
version = "0.0.2"
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
    implementation("com.h2database:h2:$h2Version")

    //Logs
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("ch.qos.logback:logback-classic:1.3.5")

    //Spek
    testImplementation ("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testRuntimeOnly ("org.spekframework.spek2:spek-runner-junit5:$spekVersion")

    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly ("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    //Firebase
    implementation ("com.google.firebase:firebase-admin:9.1.1")

    //Testing dependencies
    testImplementation ("io.mockk:mockk:$mockkVersion")
    testImplementation("io.ktor:ktor-server-host-common:$ktorVersion")
    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.ktor:ktor-client-cio:$ktorVersion")

}

sourceSets {
    create("integrationTest") {
        kotlin {
            compileClasspath += main.get().output + configurations.testRuntimeClasspath
            runtimeClasspath += output + compileClasspath
        }
    }
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs the integration tests"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    mustRunAfter(tasks["test"])
}

tasks.check {
    dependsOn(integrationTest)
}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to mainClassName)
    }
}

tasks.create("stage") {
    dependsOn(tasks.getByName("build"),tasks.getByName("clean"))
}
