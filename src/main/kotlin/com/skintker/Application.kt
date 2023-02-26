package com.skintker

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.skintker.data.db.DatabaseFactory
import com.skintker.data.components.InputValidator
import com.skintker.data.components.PaginationManager
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.repository.StatsRepository
import com.skintker.plugins.configureAdministration
import com.skintker.plugins.configureFreeMarker
import com.skintker.plugins.configureKoin
import com.skintker.plugins.configureMonitoring
import com.skintker.plugins.configureRouting
import com.skintker.plugins.configureSerialization
import io.ktor.server.application.Application
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

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
    configureFreeMarker()
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
    configureFreeMarker()
    configureAdministration()
    configureSerialization()
    configureRouting(inputValidator,paginationManager,statsRepository,reportsRepository)
}
