package com.skintker.routes.reports

import com.skintker.domain.constants.ResponseCodes
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.model.responses.LogListResponse
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.data.validators.InputValidator
import com.skintker.routes.PathParams.USER_ID_PARAM
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getReports(reportsRepository: ReportsRepository, inputValidator: InputValidator) {
    /**
     * Get all the reports from a given user token
     */
    get("/reports/{${USER_ID_PARAM}}") {
        val userId = call.parameters[USER_ID_PARAM]
        if (inputValidator.isUserIdInvalid(userId)) {
            call.respondText(
                ResponseConstants.INVALID_USER_ID_RESPONSE, status = HttpStatusCode.Unauthorized
            )
        } else {
            call.respond(
                status = HttpStatusCode.OK, ServiceResponse(
                    statusCode = ResponseCodes.NO_ERROR,
                    content = LogListResponse(reportsRepository.getReports(userId!!))
                )
            )
        }
    }
}
