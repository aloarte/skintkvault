package com.skintker.routes.report

import com.skintker.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.constants.ResponseCodes.INVALID_INPUT
import com.skintker.constants.ResponseCodes.NO_ERROR
import com.skintker.constants.ResponseConstants.BAD_INPUT_DATA
import com.skintker.constants.ResponseConstants.GENERIC_ERROR_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_INPUT_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_USER_ID_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_EDITED_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_NOT_EDITED_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_NOT_STORED_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_STORED_RESPONSE
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.dto.DailyLog
import com.skintker.data.responses.ServiceResponse
import com.skintker.data.validators.InputValidator
import com.skintker.model.SaveReportStatus
import com.skintker.routes.PathParams.USER_ID_PARAM
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.createReport(reportsRepository: ReportsRepository, inputValidator: InputValidator) {

    /**
     * Put a new report from the given user and save it on the database
     */
    put("/report/{$USER_ID_PARAM}") {
        try {
            val userId = call.parameters[USER_ID_PARAM]
            if (inputValidator.isUserIdInvalid(userId)) {
                call.respondText(INVALID_USER_ID_RESPONSE, status = HttpStatusCode.Unauthorized)
            } else {
                val log = call.receive<DailyLog>()
                val errorMessageInvalidLog = inputValidator.isLogInvalid(log)
                if(errorMessageInvalidLog!=null){
                    call.respond(
                        status = HttpStatusCode.OK, message = ServiceResponse(INVALID_INPUT, errorMessageInvalidLog)
                    )
                }else{
                    when (reportsRepository.saveReport(userId!!, log)) {
                        SaveReportStatus.Saved -> call.respond(
                            status = HttpStatusCode.Created, message = ServiceResponse(NO_ERROR, REPORT_STORED_RESPONSE)
                        )
                        SaveReportStatus.Edited -> call.respond(
                            status = HttpStatusCode.OK, message = ServiceResponse(NO_ERROR, REPORT_EDITED_RESPONSE)
                        )
                        SaveReportStatus.SavingFailed -> call.respond(
                            status = HttpStatusCode.OK, message = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_STORED_RESPONSE)
                        )
                        SaveReportStatus.EditingFailed -> call.respond(
                            status = HttpStatusCode.OK, message = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_EDITED_RESPONSE)
                        )
                        SaveReportStatus.BadInput -> call.respondText(BAD_INPUT_DATA, status = HttpStatusCode.BadRequest)
                    }
                }
            }
        } catch (exception: Exception) {
            when (exception) {
                is JsonConvertException, is BadRequestException, is CannotTransformContentToTypeException -> {
                    call.respondText(INVALID_INPUT_RESPONSE, status = HttpStatusCode.BadRequest)
                }

                else -> call.respondText(
                    "$GENERIC_ERROR_RESPONSE   ${exception.message}    ${exception.cause}",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
    }
}
