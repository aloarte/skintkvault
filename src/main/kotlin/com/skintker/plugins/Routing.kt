package com.skintker.plugins

import com.skintker.domain.repository.ReportsRepository
import com.skintker.data.components.InputValidator
import com.skintker.data.components.PaginationManager
import com.skintker.domain.repository.StatsRepository
import com.skintker.domain.UserValidator
import com.skintker.routes.redirectHome
import com.skintker.routes.report.createReport
import com.skintker.routes.report.deleteReport
import com.skintker.routes.reports.deleteReports
import com.skintker.routes.reports.getReports
import com.skintker.routes.getHome
import com.skintker.routes.removeSteps
import com.skintker.routes.staticContent
import com.skintker.routes.stats.getStats
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(
    inputValidator: InputValidator,
    userValidator: UserValidator,
    paginationManager: PaginationManager,
    statsRepository: StatsRepository,
    reportsRepository: ReportsRepository
) {

    routing {
        getHome()
        removeSteps()
        redirectHome()
        staticContent()
    }

    //Everything requested to /report
    routing {
        createReport(reportsRepository, inputValidator, userValidator)
        deleteReport(reportsRepository, userValidator)
    }

    //Everything requested to /reports
    routing {
        getReports(reportsRepository, inputValidator, userValidator, paginationManager)
        deleteReports(reportsRepository, userValidator)
    }

    // Everything requested to /stats
    routing {
        getStats(statsRepository, userValidator)
    }
}
