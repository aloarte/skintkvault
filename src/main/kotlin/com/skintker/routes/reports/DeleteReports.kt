package com.skintker.routes.reports

import com.skintker.constants.ResponseConstants
import com.skintker.exception.TokenException
import com.skintker.data.repository.ReportsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.removeReports(reportsRepository: ReportsRepository) {


    /**
     * Delete all the reports from a given user token
     */
    delete("/{$TOKEN_PATH_PARAM}") {
        try {
            val token = call.parameters[TOKEN_PATH_PARAM]
            if (token.isNullOrEmpty()) { //TODO: Verify also that is a valid token from a user and return different exceptions
                throw TokenException()
            }

            if (reportsRepository.deleteReports(token)) call.respond(HttpStatusCode.NoContent)
            else call.respondText(
                text = ResponseConstants.REPORTS_NOT_DELETED_RESPONSE,
                status = HttpStatusCode.InternalServerError
            )

        } catch (exception: Exception) {
            when (exception) {
                is TokenException -> call.respondText(
                    text = ResponseConstants.INVALID_TOKEN_RESPONSE,
                    status = HttpStatusCode.Unauthorized
                )

                else -> call.respondText(ResponseConstants.GENERIC_ERROR_RESPONSE, status = HttpStatusCode.BadRequest)
            }
        }
    }
}
