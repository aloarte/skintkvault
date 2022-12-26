package com.skintker.routes.report

import com.skintker.constants.ResponseConstants.GENERIC_ERROR_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_INPUT_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_TOKEN_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_STORED_RESPONSE
import com.skintker.exception.TokenException
import com.skintker.manager.DatabaseManager
import com.skintker.model.DailyLog
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

const val TOKEN_PATH_PARAM = "token"

fun Route.createReport(databaseManager:DatabaseManager) {

    /**
     * Put a new report from the given user token and save it on the database
     */
    put("/report/{$TOKEN_PATH_PARAM}") {
        try {
            val token = call.parameters[TOKEN_PATH_PARAM]
            val log = call.receive<DailyLog>()
            if (token.isNullOrEmpty()) {
                throw TokenException()
            }
            databaseManager.saveReport(token, log)
            call.respondText(REPORT_STORED_RESPONSE, status = HttpStatusCode.Created)
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
