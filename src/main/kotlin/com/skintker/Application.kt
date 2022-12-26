package com.skintker

import com.skintker.manager.DatabaseManager
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.skintker.plugins.*
import org.koin.ktor.ext.inject

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val databaseManager by inject<DatabaseManager>()


//    configureMonitoring()
    configureKoin()
    configureAdministration()
    configureSerialization()
    configureSecurity()
    configureRouting(databaseManager)
}
