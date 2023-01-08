package com.skintker.routes.report

import com.skintker.constants.ResponseConstants.BAD_INPUT_DATA
import com.skintker.constants.ResponseConstants.GENERIC_ERROR_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_INPUT_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_TOKEN_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_EDITED_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_NOT_EDITED_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_NOT_STORED_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_STORED_RESPONSE
import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.datasources.impl.LogsDatasourceImpl
import com.skintker.exception.TokenException
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.dto.DailyLog
import com.skintker.model.SaveReportStatus
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val TOKEN_PATH_PARAM = "token"

fun Route.createReport(reportsRepository: ReportsRepository) {


    val dao: LogsDatasource = LogsDatasourceImpl()


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
            when(reportsRepository.saveReport(token, log)){
                SaveReportStatus.Saved -> call.respondText(REPORT_STORED_RESPONSE, status = HttpStatusCode.Created)
                SaveReportStatus.Edited -> call.respondText(REPORT_EDITED_RESPONSE, status = HttpStatusCode.Accepted)
                SaveReportStatus.SavingFailed -> call.respondText(REPORT_NOT_STORED_RESPONSE, status = HttpStatusCode.BadRequest)
                SaveReportStatus.EditingFailed -> call.respondText(REPORT_NOT_EDITED_RESPONSE, status = HttpStatusCode.BadRequest)
                SaveReportStatus.BadInput ->  call.respondText(BAD_INPUT_DATA, status = HttpStatusCode.BadRequest)
            }

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
