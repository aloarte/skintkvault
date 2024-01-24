package com.skintker.routes.user

import com.skintker.data.dto.user.UserDto
import com.skintker.domain.constants.ResponseCodes
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.domain.UserManager
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.constants.ResponseConstants.INVALID_USER_ID_OR_TOKEN_RESPONSE
import com.skintker.domain.model.responses.UserResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.application.call
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.put
import org.slf4j.LoggerFactory

fun Route.createUser(userManager: UserManager) {

    /**
     * Create a new user into the database validating the token previously
     */
    put("/user/fb") {
        val logger = LoggerFactory.getLogger("Route.createReport")
        try {
            val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
            if (token != null) {
                val userDto = call.receive<UserDto>()
                call.respond(
                    status = HttpStatusCode.OK,
                    message = UserResponse(userManager.addUser(userToken = token, userId = userDto.userId))
                )
            } else {
                logger.error("Invalid user input data $INVALID_USER_ID_OR_TOKEN_RESPONSE")
                call.respondText(
                    INVALID_USER_ID_OR_TOKEN_RESPONSE,
                    status = HttpStatusCode.BadRequest
                )
            }
        } catch (exception: JsonConvertException) {
            logger.error("Returned 400. JsonConvertException: $exception")
            call.respondText(ResponseConstants.INVALID_INPUT_RESPONSE, status = HttpStatusCode.BadRequest)

        } catch (exception: BadRequestException) {
            logger.error("Returned 400. BadRequestException: $exception")
            call.respondText(ResponseConstants.INVALID_INPUT_RESPONSE, status = HttpStatusCode.BadRequest)

        }
    }
}

