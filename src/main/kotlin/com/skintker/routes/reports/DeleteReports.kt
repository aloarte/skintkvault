package com.skintker.routes.reports

import com.skintker.domain.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.constants.ResponseConstants.REPORTS_DELETED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORTS_NOT_DELETED_RESPONSE
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.data.components.InputValidator
import com.skintker.routes.PathParams.USER_ID_PARAM
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import org.slf4j.LoggerFactory

fun Route.deleteReports(reportsRepository: ReportsRepository, inputValidator: InputValidator) {

    /**
     * Delete all the reports from a given user token
     */
    delete("/reports/{${USER_ID_PARAM}}") {
        val logger = LoggerFactory.getLogger("Route.deleteReports")
        val userId = call.parameters[USER_ID_PARAM]
        if (inputValidator.isUserIdInvalid(userId)) {
            call.respondText(
                ResponseConstants.INVALID_USER_ID_RESPONSE,
                status = HttpStatusCode.Unauthorized
            )
        } else {
            call.respond(
                status = HttpStatusCode.OK,
                message = if (reportsRepository.deleteReports(userId!!)) {
                    ServiceResponse(NO_ERROR, REPORTS_DELETED_RESPONSE)
                } else {
                    logger.error("Returned 200 with a database error: $DATABASE_ISSUE - $REPORTS_NOT_DELETED_RESPONSE")
                    ServiceResponse(DATABASE_ISSUE, REPORTS_NOT_DELETED_RESPONSE)
                }
            )
        }
    }
}
