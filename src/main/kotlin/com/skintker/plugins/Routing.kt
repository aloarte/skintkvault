package com.skintker.plugins

import com.skintker.domain.repository.ReportsRepository
import com.skintker.data.components.InputValidator
import com.skintker.data.components.PaginationManager
import com.skintker.domain.repository.StatsRepository
import com.skintker.domain.UserManager
import com.skintker.routes.redirectHome
import com.skintker.routes.report.createReport
import com.skintker.routes.report.deleteReport
import com.skintker.routes.reports.deleteReports
import com.skintker.routes.reports.getReports
import com.skintker.routes.getHome
import com.skintker.routes.privacyPolicy
import com.skintker.routes.removeSteps
import com.skintker.routes.staticContent
import com.skintker.routes.stats.getStats
import com.skintker.routes.user.createUser
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(
    inputValidator: InputValidator,
    userManager: UserManager,
    paginationManager: PaginationManager,
    statsRepository: StatsRepository,
    reportsRepository: ReportsRepository
) {

    routing {
        getHome()
        removeSteps()
        privacyPolicy()
        redirectHome()
        staticContent()
    }

    //Everything requested to /report
    routing {
        createReport(reportsRepository, inputValidator, userManager)
        deleteReport(reportsRepository, userManager)
    }

    //Everything requested to /reports
    routing {
        getReports(reportsRepository, inputValidator, userManager, paginationManager)
        deleteReports(reportsRepository, userManager)
    }

    // Everything requested to /stats
    routing {
        getStats(statsRepository, userManager)
    }

    // Everything requested to /user
    routing {
        createUser(userManager)
    }

}
