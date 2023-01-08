package com.skintker.routes.reports

import com.skintker.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.constants.ResponseCodes.NO_ERROR
import com.skintker.constants.ResponseConstants
import com.skintker.constants.ResponseConstants.REPORTS_DELETED_RESPONSE
import com.skintker.constants.ResponseConstants.REPORTS_NOT_DELETED_RESPONSE
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.responses.ServiceResponse
import com.skintker.data.validators.UserInfoValidator
import com.skintker.routes.PathParams.USER_ID_PARAM
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteReports(reportsRepository: ReportsRepository, userInfoValidator: UserInfoValidator) {

    /**
     * Delete all the reports from a given user token
     */
    delete("/reports/{${USER_ID_PARAM}}") {
        val userId = call.parameters[USER_ID_PARAM]
        if (userInfoValidator.isUserIdInvalid(userId)) {
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
                    ServiceResponse(DATABASE_ISSUE, REPORTS_NOT_DELETED_RESPONSE)
                }
            )
        }
    }
}
