package com.skintker.routes.reports

import com.skintker.constants.ResponseConstants
import com.skintker.exception.TokenException
import com.skintker.data.repository.ReportsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val TOKEN_PATH_PARAM = "token"

fun Route.getReports(reportsRepository: ReportsRepository) {
    /**
     * Get all the reports from a given user token
     */
    get("/{$TOKEN_PATH_PARAM}") {
        try {
            val token = call.parameters[TOKEN_PATH_PARAM]
            if (token.isNullOrEmpty()) { //TODO: Verify also that is a valid token from a user and return different exceptions
                throw TokenException()
            }

            val userReportList = reportsRepository.getReports(token)
            call.respond(status = HttpStatusCode.OK, userReportList)
        } catch (exception: Exception) {
            when (exception) {
                is TokenException -> call.respondText(
                    ResponseConstants.INVALID_TOKEN_RESPONSE,
                    status = HttpStatusCode.Unauthorized
                )

                else -> call.respondText(ResponseConstants.GENERIC_ERROR_RESPONSE, status = HttpStatusCode.BadRequest)
            }
        }
    }
}
