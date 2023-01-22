package com.skintker.plugins

import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.application.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call ->
            call.request.path().contains("/images").not() && call.request.path().contains("/assets").not()
        }
        format { call ->
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            val status = call.response.status()
            val queryString = call.request.queryString()
            val userAgent = call.request.headers["User-Agent"]
            "[CallLogging] [$httpMethod] $path : $status. $queryString  - ($userAgent)"
        }
    }

}
