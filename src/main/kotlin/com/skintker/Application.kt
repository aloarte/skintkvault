package com.skintker

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.skintker.data.db.DatabaseFactory
import com.skintker.data.components.InputValidator
import com.skintker.data.components.PaginationManager
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.repository.StatsRepository
import com.skintker.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::initModuleProd)
        .start(wait = true)
}

fun Application.initModuleProd(){
    val statsRepository by inject<StatsRepository>()
    val reportsRepository by inject<ReportsRepository>()
    val inputValidator by inject<InputValidator>()
    val paginationManager by inject<PaginationManager>()

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build()

    FirebaseApp.initializeApp(options)

    DatabaseFactory.init(false)
    configureMonitoring()
    configureKoin()
    configureAdministration()
    configureSerialization()
    configureRouting(inputValidator,paginationManager,statsRepository,reportsRepository)
}

fun Application.initModuleTest() {
    val statsRepository by inject<StatsRepository>()
    val reportsRepository by inject<ReportsRepository>()
    val inputValidator by inject<InputValidator>()
    val paginationManager by inject<PaginationManager>()

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build()

    FirebaseApp.initializeApp(options)

    DatabaseFactory.init(true)
    configureMonitoring()
    configureKoin()
    configureAdministration()
    configureSerialization()
    configureRouting(inputValidator,paginationManager,statsRepository,reportsRepository)
}
