package com.skintker

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.skintker.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
//    configureMonitoring()
    configureKoin()
    configureAdministration()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
