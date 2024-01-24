package com.skintker.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.origin
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.queryString
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        filter { call ->

            call.request.path().contains("/images").not()
                    && call.request.path().contains("/assets").not()
                    && call.request.path().contains("/fonts").not()
        }
        format { call ->
            val suspicious  = isSuspiciousPath(call.request.path())
            level = if(suspicious) Level.WARN else Level.INFO

            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            val status = call.response.status()
            val queryString = call.request.queryString()
            val userAgent = call.request.headers["User-Agent"]

            if(suspicious){
                val ip = call.request.origin.remoteHost
                val address = call.request.origin.remoteAddress
                val uri = call.request.origin.uri
                val host = call.request.origin.serverHost
                "<SUSPICIOUS> [$httpMethod] $path$queryString : $status ($userAgent). Came from $ip, $address, $host. Executed: $uri"
            }
            else{
                "[API CALL] [$httpMethod] $path$queryString : $status ($userAgent)"
            }

        }
    }

}

fun isSuspiciousPath(path: String): Boolean {
    val suspiciousPaths = setOf("/skt","/privacypolicy","/remove","/report", "/reports", "/stats", "/user/fb", "/user/wipe")
    return suspiciousPaths.any { path.contains(it) }.not()
}