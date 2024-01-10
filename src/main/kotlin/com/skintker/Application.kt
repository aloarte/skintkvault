package com.skintker

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.skintker.data.db.DatabaseFactory
import com.skintker.data.components.InputValidator
import com.skintker.data.components.PaginationManager
import com.skintker.data.db.DdbbConfig
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.repository.StatsRepository
import com.skintker.plugins.configureAdministration
import com.skintker.plugins.configureFreeMarker
import com.skintker.plugins.configureKoin
import com.skintker.plugins.configureMonitoring
import com.skintker.plugins.configureRouting
import com.skintker.plugins.configureSerialization
import com.skintker.domain.UserValidator
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.ext.inject
import java.lang.System.getenv

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::initProdMode)
        .start(wait = true)
    println("-- SERVER STARTED --")
}

fun Application.initProdMode() {
    println("-- SERVER CONFIGURED IN PRODUCTION MODE --")
    initFirebase()
    initDatabase(false)
    initModules()
}

fun Application.initTestMode() {
    println("-- SERVER CONFIGURED IN TEST MODE --")
    initFirebase()
    initDatabase(false)
    initModules()
}


private fun Application.initModules() {
    val statsRepository by inject<StatsRepository>()
    val reportsRepository by inject<ReportsRepository>()
    val inputValidator by inject<InputValidator>()
    val userValidator by inject<UserValidator>()
    val paginationManager by inject<PaginationManager>()
    configureMonitoring()
    configureKoin()
    configureFreeMarker()
    configureAdministration()
    configureSerialization()
    configureRouting(inputValidator,userValidator, paginationManager, statsRepository, reportsRepository)
}

private fun initFirebase() {
//    FirebaseOptions.builder()
//        .setCredentials(GoogleCredentials.getApplicationDefault())
//        .build().also {
//            FirebaseApp.initializeApp(it)
//        }
}

private fun initDatabase(isProduction: Boolean) {
    DatabaseFactory.init(
        isProduction = isProduction,
        config = if (isProduction) {
            getDatabaseConfig()
        } else {
            DdbbConfig("", "", "", "","")
        }
    )
}

private fun getDatabaseConfig(): DdbbConfig {
    val user = getenv("DDBB_USER")
    val password = getenv("DDBB_PWD")
    val databaseName = getenv("DDBB_NAME")
    val databasePort = getenv("DDBB_PORT")
    val containerName = getenv("DDBB_CONTAINER")

    val userDataNotEmpty = user != null && password != null
    val databaseDataNotEmpty = databaseName != null && databasePort != null && containerName!=null

    return if (userDataNotEmpty && databaseDataNotEmpty) {
        DdbbConfig(
            userName = user,
            password = password,
            databaseName = databaseName,
            databasePort = databasePort,
            containerName = containerName
        )
    } else {
        DdbbConfig(
            userName = "",
            password = "password",
            databaseName = "databaseName",
            databasePort = "databasePort",
            containerName = "containerName"
        )
    /*throw InstantiationException("Database initialization failed due to empty parameters")*/
    }
}
