package com.skintker.routes

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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

