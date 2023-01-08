package com.skintker.routes.report

import com.skintker.constants.ResponseConstants.GENERIC_ERROR_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_INPUT_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_TOKEN_RESPONSE
import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.datasources.impl.LogsDatasourceImpl
import com.skintker.exception.TokenException
import com.skintker.data.repository.ReportsRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteReport(reportsRepository: ReportsRepository) {

    /**
     * Put a new report from the given user token and save it on the database
     */
    get("/report/{$TOKEN_PATH_PARAM}") {
        try {
            val token = call.parameters[TOKEN_PATH_PARAM]
            if (token.isNullOrEmpty()) {
                throw TokenException()
            }

            val reports = reportsRepository.deleteReports(token)
            call.respond(status = HttpStatusCode.OK, reports)


        } catch (exception: Exception) {
            when (exception) {
                is JsonConvertException,
                is BadRequestException,
                is CannotTransformContentToTypeException -> {
                    call.respondText(INVALID_INPUT_RESPONSE, status = HttpStatusCode.BadRequest)
                }
                is TokenException -> call.respondText(INVALID_TOKEN_RESPONSE, status = HttpStatusCode.Unauthorized)
                else -> call.respondText("$GENERIC_ERROR_RESPONSE   ${exception.message}    ${exception.cause}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}
