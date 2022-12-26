package com.skintker.plugins

import com.skintker.di.database
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    // Install Ktor features
    install(Koin) {
//        slf4jLogger()
        modules(database)
    }

}
