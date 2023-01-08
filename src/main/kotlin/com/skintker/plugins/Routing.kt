package com.skintker.plugins

import com.skintker.data.repository.ReportsRepository
import com.skintker.data.validators.UserInfoValidator
import com.skintker.routes.home
import com.skintker.routes.report.createReport
import com.skintker.routes.report.deleteReport
import com.skintker.routes.reports.deleteReports
import com.skintker.routes.reports.getReports
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    reportsRepository: ReportsRepository,
    userInfoValidator: UserInfoValidator

) {

    routing {
        home()
    }

    //Everything requested to /report
    routing {
        createReport(reportsRepository,userInfoValidator)
        deleteReport(reportsRepository,userInfoValidator)
    }

    //Everything requested to /reports
    routing {
        getReports(reportsRepository,userInfoValidator)
        deleteReports(reportsRepository,userInfoValidator)
    }
}
