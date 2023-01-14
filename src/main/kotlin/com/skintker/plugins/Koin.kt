package com.skintker.plugins

import com.skintker.di.dao
import com.skintker.di.repository
import com.skintker.di.single
import com.skintker.di.validators
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    // Install Ktor features
    install(Koin) {
//        slf4jLogger()
        modules(dao,repository, validators, single)
    }

}
