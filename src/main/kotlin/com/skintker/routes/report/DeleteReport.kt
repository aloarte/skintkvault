package com.skintker.routes.report

import com.skintker.constants.ResponseCodes
import com.skintker.constants.ResponseConstants
import com.skintker.constants.ResponseConstants.INVALID_PARAM_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_USER_ID_RESPONSE
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.responses.ServiceResponse
import com.skintker.data.validators.InputValidator
import com.skintker.model.LogIdValues
import com.skintker.routes.PathParams.USER_ID_PARAM
import com.skintker.routes.QueryParams.LOG_DATE_PARAM
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteReport(reportsRepository: ReportsRepository, inputValidator: InputValidator) {

    /**
     * Delete the given report from a given user
     */
    delete("/report/{${USER_ID_PARAM}}") {
        val userId = call.parameters[USER_ID_PARAM]
        if (inputValidator.isUserIdInvalid(userId)) {
            call.respondText(
                INVALID_USER_ID_RESPONSE,
                status = HttpStatusCode.Unauthorized
            )
        } else {
            call.request.queryParameters[LOG_DATE_PARAM]?.let { logDate->
                call.respond(
                    status = HttpStatusCode.OK,
                    message = if (reportsRepository.deleteReport(LogIdValues(logDate, userId!!))) {
                        ServiceResponse(ResponseCodes.NO_ERROR, ResponseConstants.REPORT_DELETED_RESPONSE)
                    } else {
                        ServiceResponse(ResponseCodes.DATABASE_ISSUE, ResponseConstants.REPORT_NOT_DELETED_RESPONSE)
                    }
                )
            } ?: run {
                call.respondText(
                    INVALID_PARAM_RESPONSE,
                    status = HttpStatusCode.BadRequest
                )
            }

        }
    }
}
