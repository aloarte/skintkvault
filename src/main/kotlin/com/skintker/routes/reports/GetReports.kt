package com.skintker.routes.reports

import com.skintker.data.components.PaginationManager
import com.skintker.domain.constants.ResponseCodes
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.model.responses.LogListResponse
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.data.components.InputValidator
import com.skintker.routes.PathParams.USER_ID_PARAM
import com.skintker.routes.QueryParams
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.getReports(
    reportsRepository: ReportsRepository,
    inputValidator: InputValidator,
    paginationManager: PaginationManager
) {
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
            val paramLimit = call.request.queryParameters[QueryParams.LIMIT_PARAM]
            val paramOffset = call.request.queryParameters[QueryParams.OFFSET_PARAM]
            val logList = reportsRepository.getReports(userId!!)
            val responseLogList = if (paramLimit != null && paramOffset != null) {
                if (inputValidator.arePaginationIndexesInvalid(paramLimit,paramOffset, logList.size)) {
                    call.respondText(ResponseConstants.BAD_INPUT_DATA, status = HttpStatusCode.BadRequest)
                    return@get
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
