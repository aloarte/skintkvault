package com.skintker

import com.skintker.data.db.DatabaseFactory
import com.skintker.data.repository.ReportsRepository
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
    val reportsRepository by inject<ReportsRepository>()

    DatabaseFactory.init()
//    configureMonitoring()
    configureKoin()
    configureAdministration()
    configureSerialization()
    configureSecurity()
    configureRouting(reportsRepository)
}
