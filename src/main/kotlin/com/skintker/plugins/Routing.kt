package com.skintker.plugins

import com.skintker.domain.repository.ReportsRepository
import com.skintker.data.validators.InputValidator
import com.skintker.domain.repository.StatsRepository
import com.skintker.routes.home
import com.skintker.routes.report.createReport
import com.skintker.routes.report.deleteReport
import com.skintker.routes.reports.deleteReports
import com.skintker.routes.reports.getReports
import com.skintker.routes.stats.getStats
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    statsRepository: StatsRepository,
    reportsRepository: ReportsRepository,
    inputValidator: InputValidator

) {

    routing {
        home()
    }

    //Everything requested to /report
    routing {
        createReport(reportsRepository,inputValidator)
        deleteReport(reportsRepository,inputValidator)
    }

    //Everything requested to /reports
    routing {
        getReports(reportsRepository,inputValidator)
        deleteReports(reportsRepository,inputValidator)
    }

    // Everything requested to /stats
    routing {
        getStats(statsRepository,inputValidator)
    }
}
