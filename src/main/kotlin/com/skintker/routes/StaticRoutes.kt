package com.skintker.routes

import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Route.staticContent(){

        static("/") {
            staticBasePackage = "static"
            resource("index.html")
            defaultResource("index.html")
            static("images") {
                resource("logo.png")
//                resource("favicon.ico")
            }
            static("assets") {
                resources("css")
                resources("js")
            }
        }
}