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
import io.ktor.server.application.*
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.System.getenv

fun main() {
    println("-- SERVER STARTED --")

    embeddedServer(factory = Netty, environment = configureSSLEnvironment()).start(wait = true)


//    embeddedServer(Netty, port = 8080, module = Application::initModuleProd)
//        .start(wait = true)



}

fun Application.initModuleProd() {


    println("-- SERVER CONFIGURED IN PRODUCTION MODE ")
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
    configureRouting(inputValidator, paginationManager, statsRepository, reportsRepository)
}

private fun configureSSLEnvironment(): ApplicationEngineEnvironment {
    return applicationEngineEnvironment {

        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8080
        }

        val ksPath = getenv("KS_PATH") ?: ""
        val ksPassword = getenv("KS_PASSWORD") ?: ""
        val ksAlias = getenv("KS_ALIAS") ?: ""
        val ksPkPassword = getenv("KS_PK_PASSWORD") ?: ""

        sslConnector(
            keyStore = SslSettings.getKeyStore(ksPath, ksPassword),
            keyAlias = ksAlias,
            keyStorePassword = { ksPassword.toCharArray() },
            privateKeyPassword = { ksPkPassword.toCharArray() }
        ) {
//            this.keyStorePath = ?????????????
            port = 8443
        }
        module(Application::initModuleProd)
    }
}


fun Application.initModuleTest() {
    println("-- SERVER CONFIGURED IN TEST MODE --")
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
    configureRouting(inputValidator, paginationManager, statsRepository, reportsRepository)
}
