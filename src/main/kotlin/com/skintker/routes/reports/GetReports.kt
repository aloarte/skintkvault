package com.skintker.routes.reports

import com.skintker.data.components.PaginationManager
import com.skintker.domain.constants.ResponseCodes
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.model.responses.LogListResponse
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.data.components.InputValidator
import com.skintker.domain.constants.ResponseConstants.BAD_INPUT_DATA
import com.skintker.routes.PathParams.USER_ID_PARAM
import com.skintker.routes.QueryParams
import com.skintker.domain.UserValidator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.slf4j.LoggerFactory

fun Route.getReports(
    reportsRepository: ReportsRepository,
    inputValidator: InputValidator,
    userValidator: UserValidator,
    paginationManager: PaginationManager
) {
    /**
     * Get all the reports from a given user token
     */
    get("/reports/{${USER_ID_PARAM}}") {
        val logger = LoggerFactory.getLogger("Route.getReports")
        val userId = call.parameters[USER_ID_PARAM]
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        userValidator.verifyUser(call = call, logger = logger, userId = userId, userToken = token) {
            val paramLimit = call.request.queryParameters[QueryParams.LIMIT_PARAM]
            val paramOffset = call.request.queryParameters[QueryParams.OFFSET_PARAM]
            val logList = reportsRepository.getReports(userId!!)
            val responseLogList = if (paramLimit != null && paramOffset != null) {
                if (inputValidator.arePaginationIndexesInvalid(paramLimit, paramOffset, logList.size)) {
                    logger.error("Returned 400. $BAD_INPUT_DATA")
                    call.respondText(BAD_INPUT_DATA, status = HttpStatusCode.BadRequest)
                    return@verifyUser
                }
                paginationManager.getPageFromLogs(paramLimit, paramOffset, logList)
            } else {
                logList
            }

            call.respond(
                HttpStatusCode.OK,
                ServiceResponse(
                    statusCode = ResponseCodes.NO_ERROR, content = LogListResponse(responseLogList, logList.size)
                )
            )
        }


    }
}
