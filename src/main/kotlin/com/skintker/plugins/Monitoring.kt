package com.skintker.plugins


import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.queryString
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call ->
            call.request.path().contains("/images").not()
                    && call.request.path().contains("/assets").not()
                    && call.request.path().contains("/fonts").not()
        }
        format { call ->
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            val status = call.response.status()
            val queryString = call.request.queryString()
            val userAgent = call.request.headers["User-Agent"]
            "[API CALL] [$httpMethod] $path$queryString : $status ($userAgent)"
        }
    }
}
