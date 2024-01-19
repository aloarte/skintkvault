package com.skintker.routes.user

import com.skintker.domain.constants.ResponseCodes
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.routes.PathParams.USER_ID_PARAM
import com.skintker.domain.UserManager
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.constants.ResponseConstants.INVALID_USER_ID_OR_TOKEN_RESPONSE
import com.skintker.domain.constants.ResponseConstants.USER_NOT_CREATED_RESPONSE
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.put
import org.slf4j.LoggerFactory

fun Route.createUser(userManager: UserManager) {

    /**
     * Create a new user into the database validating the token previously
     */
    put("/user/{$USER_ID_PARAM}") {
        val logger = LoggerFactory.getLogger("Route.createReport")
        val userId = call.parameters[USER_ID_PARAM]
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
        if (userId != null && token != null) {
            if (userManager.addUser(userToken = token, userId = userId)) {
                call.respond(
                    status = HttpStatusCode.Created,
                    message = ServiceResponse(
                        ResponseCodes.NO_ERROR,
                        ResponseConstants.USER_CREATED_RESPONSE
                    )
                )
            } else {
                logger.error("User not created. $USER_NOT_CREATED_RESPONSE")
                call.respondText(
                    USER_NOT_CREATED_RESPONSE,
                    status = HttpStatusCode.Conflict
                )

            }
        } else {
            logger.error("Invalid user input data $INVALID_USER_ID_OR_TOKEN_RESPONSE")
            call.respondText(
                INVALID_USER_ID_OR_TOKEN_RESPONSE,
                status = HttpStatusCode.BadRequest
            )
        }

    }
}

