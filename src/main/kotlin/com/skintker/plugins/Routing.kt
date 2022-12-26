package com.skintker.plugins

import com.skintker.routes.home
import com.skintker.routes.report.createReport
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {

    routing {
        home()
    }

    //Everything requested to /report
    routing {
        createReport()
    }
}
