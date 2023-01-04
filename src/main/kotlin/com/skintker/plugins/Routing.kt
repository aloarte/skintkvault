package com.skintker.plugins

import com.skintker.data.repository.ReportsRepository
import com.skintker.routes.home
import com.skintker.routes.report.createReport
import com.skintker.routes.report.getReport
import com.skintker.routes.reports.removeReports
import com.skintker.routes.reports.retrieveReports
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    reportsRepository: ReportsRepository
) {

    routing {
        home()
    }

    //Everything requested to /report
    routing {
        createReport(reportsRepository)
        getReport(reportsRepository)
    }

    routing {
        route("/reports"){
            retrieveReports(reportsRepository)
            removeReports(reportsRepository)
        }

    }
}
