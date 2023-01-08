package com.skintker.plugins

import com.skintker.data.repository.ReportsRepository
import com.skintker.routes.home
import com.skintker.routes.report.createReport
import com.skintker.routes.report.deleteReport
import com.skintker.routes.reports.removeReports
import com.skintker.routes.reports.getReports
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
        deleteReport(reportsRepository)
    }

    routing {
        route("/reports"){
            getReports(reportsRepository)
            removeReports(reportsRepository)
        }

    }
}
