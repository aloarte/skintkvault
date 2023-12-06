package com.skintker.routes.stats

import com.skintker.data.IRRITATION_THRESHOLD
import com.skintker.domain.constants.ResponseCodes
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.domain.model.responses.StatsResponse
import com.skintker.domain.repository.StatsRepository
import com.skintker.routes.PathParams.USER_ID_PARAM
import com.skintker.domain.UserValidator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.slf4j.LoggerFactory

fun Route.getStats(statsRepository: StatsRepository, validator: UserValidator) {
    /**
     * Get the stats from a given user
     */
    get("/stats/{${USER_ID_PARAM}}") {
        val logger = LoggerFactory.getLogger("Route.getStats")
        val irritationThreshold = call.request.queryParameters["threshold"]?.toIntOrNull() ?: IRRITATION_THRESHOLD
        val userId = call.parameters[USER_ID_PARAM]
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        validator.verifyUser(call = call, logger = logger, userId = userId, userToken = token) {
            call.respond(
                status = HttpStatusCode.OK, ServiceResponse(
                    statusCode = ResponseCodes.NO_ERROR,
                    content = StatsResponse(statsRepository.calculateUserStats(userId!!, irritationThreshold))
                )
            )
        }
    }
}
