package com.skintker.routes.stats

import com.skintker.data.IRRITATION_THRESHOLD
import com.skintker.domain.constants.ResponseCodes
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.data.components.InputValidator
import com.skintker.domain.model.responses.StatsResponse
import com.skintker.domain.repository.StatsRepository
import com.skintker.routes.PathParams.USER_ID_PARAM
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.getStats(statsRepository: StatsRepository, inputValidator: InputValidator) {
    /**
     * Get the stats from a given user
     */
    get("/stats/{${USER_ID_PARAM}}") {
        val irritationThreshold = call.request.queryParameters["threshold"]?.toIntOrNull() ?: IRRITATION_THRESHOLD
        val userId = call.parameters[USER_ID_PARAM]
        if (inputValidator.isUserIdInvalid(userId)) {
            call.respondText(
                ResponseConstants.INVALID_USER_ID_RESPONSE, status = HttpStatusCode.Unauthorized
            )
        } else {
            call.respond(
                status = HttpStatusCode.OK, ServiceResponse(
                    statusCode = ResponseCodes.NO_ERROR,
                    content = StatsResponse(statsRepository.calculateUserStats(userId!!,irritationThreshold))
                )
            )
        }
    }
}
