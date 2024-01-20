package com.skintker.routes.report

import com.skintker.domain.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.domain.constants.ResponseCodes.INVALID_INPUT
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants.BAD_INPUT_DATA
import com.skintker.domain.constants.ResponseConstants.DATABASE_ERROR
import com.skintker.domain.constants.ResponseConstants.INVALID_INPUT_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_EDITED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_NOT_EDITED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_NOT_STORED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_STORED_RESPONSE
import com.skintker.domain.repository.ReportsRepository
import com.skintker.data.dto.logs.DailyLog
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.data.components.InputValidator
import com.skintker.domain.model.SaveReportStatus
import com.skintker.routes.PathParams.USER_ID_PARAM
import com.skintker.domain.UserManager
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.CannotTransformContentToTypeException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.put
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.statements.BatchDataInconsistentException
import org.slf4j.LoggerFactory

fun Route.createReport(
    reportsRepository: ReportsRepository,
    inputValidator: InputValidator,
    userManager: UserManager
) {

    /**
     * Put a new report from the given user and save it on the database
     */
    put("/report/{$USER_ID_PARAM}") {
        val logger = LoggerFactory.getLogger("Route.createReport")
        try {
            val userId = call.parameters[USER_ID_PARAM]
            val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
            userManager.verifyUser(call = call, logger = logger, userId = userId, userToken = token) {
                val log = call.receive<DailyLog>()
                val errorMessageInvalidLog = inputValidator.isLogInvalid(log)
                if (errorMessageInvalidLog != null) {
                    logger.error("Error $INVALID_INPUT. $errorMessageInvalidLog")
                    call.respond(
                        status = HttpStatusCode.OK, message = ServiceResponse(INVALID_INPUT, errorMessageInvalidLog)
                    )
                } else {
                    answerSaveReportResult(call, reportsRepository.saveReport(userId!!, log))
                }
            }

        } catch (exception: JsonConvertException) {
            logger.error("Returned 400. JsonConvertException: $exception")
            call.respondText(INVALID_INPUT_RESPONSE, status = HttpStatusCode.BadRequest)

        } catch (exception: BadRequestException) {
            logger.error("Returned 400. BadRequestException: $exception")
            call.respondText(INVALID_INPUT_RESPONSE, status = HttpStatusCode.BadRequest)

        } catch (exception: CannotTransformContentToTypeException) {
            logger.error("Returned 400. CannotTransformContentToTypeException: $exception")
            call.respondText(INVALID_INPUT_RESPONSE, status = HttpStatusCode.BadRequest)

        } catch (exception: BatchDataInconsistentException) {
            logger.error("Returned 200 with a database error. BatchDataInconsistentException: $exception")
            call.respond(
                status = HttpStatusCode.OK, message = ServiceResponse(DATABASE_ISSUE, DATABASE_ERROR)
            )
        } catch (exception: ExposedSQLException) {
            logger.error("Returned 200 with a database error. ExposedSQLException: $exception")
            call.respond(
                status = HttpStatusCode.OK, message = ServiceResponse(DATABASE_ISSUE, DATABASE_ERROR)
            )
        }
    }
}

private suspend fun answerSaveReportResult(call: ApplicationCall, status: SaveReportStatus) {
    when (status) {
        SaveReportStatus.Saved -> call.respond(
            status = HttpStatusCode.Created, message = ServiceResponse(NO_ERROR, REPORT_STORED_RESPONSE)
        )

        SaveReportStatus.Edited -> call.respond(
            status = HttpStatusCode.OK, message = ServiceResponse(NO_ERROR, REPORT_EDITED_RESPONSE)
        )

        SaveReportStatus.SavingFailed -> call.respond(
            status = HttpStatusCode.OK,
            message = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_STORED_RESPONSE)
        )

        SaveReportStatus.EditingFailed -> call.respond(
            status = HttpStatusCode.OK,
            message = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_EDITED_RESPONSE)
        )

        SaveReportStatus.BadInput -> call.respondText(
            BAD_INPUT_DATA,
            status = HttpStatusCode.BadRequest
        )
    }
}
