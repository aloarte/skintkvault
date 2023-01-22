package com.skintker.plugins

import com.skintker.di.dao
import com.skintker.di.repository
import com.skintker.di.single
import com.skintker.di.components
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(dao,repository, components, single)
    }

}
