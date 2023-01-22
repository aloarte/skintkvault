package com.skintker.routes

import io.ktor.server.application.call
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.http.content.resource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticBasePackage
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.staticContent() {

    static("/") {
        staticBasePackage = "static"
        static("images") {
            resource("logo.png")
            resource("favicon.ico")
        }
        static("assets") {
            resources("css")
            resources("js")
        }
    }
}

fun Route.getHome() {
    get("/home") {
        call.respond(FreeMarkerContent("index.html", mapOf<Unit, Unit>()))

    }
}

fun Route.redirectHome() {
    get("/") {
        call.respondRedirect("/home")
    }
}

