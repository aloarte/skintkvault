package com.skintker.domain

import com.skintker.domain.constants.ResponseConstants.INVALID_USER_ID_RESPONSE
import com.skintker.domain.constants.ResponseConstants.INVALID_USER_TOKEN_RESPONSE
import com.skintker.domain.repository.UserRepository
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import io.ktor.http.HttpStatusCode
import org.slf4j.Logger

class UserValidator(private val userRepository: UserRepository) {
    suspend fun verifyUser(
        call: ApplicationCall,
        logger: Logger,
        userToken: String?,
        userId: String?,
        execute: suspend () -> Unit
    ) {
        if (userRepository.isTokenValid(userToken)) {
            if (userRepository.isUserValid(userId)) {
                execute()
            } else {
                logger.error("Returned 401. $INVALID_USER_ID_RESPONSE")
                call.respondText(
                    INVALID_USER_ID_RESPONSE, status = HttpStatusCode.Unauthorized
                )
            }
        } else {
            logger.error("Returned 401. $INVALID_USER_TOKEN_RESPONSE")
            call.respondText(
                text= INVALID_USER_TOKEN_RESPONSE, status = HttpStatusCode.Unauthorized
            )
        }
    }

}
