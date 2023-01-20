package com.skintker.plugins

import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.application.*

fun Application.configureMonitoring() {
    install(CallLogging) {
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }
//    install(CallLogging) {
//        level = Level.INFO
//        filter { call -> call.request.path().startsWith("/") }
//    }
}
