package com.skintker.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.ShutDownUrl

fun Application.configureAdministration() {
    val shutdownPath = System.getenv("SHUTDOWN_PATH")

    install(ShutDownUrl.ApplicationCallPlugin) {
        shutDownUrl = shutdownPath
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }

}
