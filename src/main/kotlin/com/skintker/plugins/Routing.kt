package com.skintker.plugins

import com.skintker.manager.DatabaseManager
import com.skintker.routes.home
import com.skintker.routes.report.createReport
import com.skintker.routes.reports.retrieveReports
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    databaseManager: DatabaseManager
) {

    routing {
        home()
    }

    //Everything requested to /report
    routing {
        createReport(databaseManager)
        retrieveReports(databaseManager)
    }
}
