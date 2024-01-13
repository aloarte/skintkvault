package com.skintker.routes.reports

import com.skintker.domain.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants.REPORTS_DELETED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORTS_NOT_DELETED_RESPONSE
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.routes.PathParams.USER_ID_PARAM
import com.skintker.domain.UserValidator
import com.skintker.domain.repository.UserRepository
import com.skintker.routes.EmailRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import org.slf4j.LoggerFactory

fun Route.deleteReports(reportsRepository: ReportsRepository, userRepository: UserRepository,userValidator: UserValidator) {

    /**
     * Delete all the reports from a given user token
     */
    delete("/reports/{${USER_ID_PARAM}}") {
        val logger = LoggerFactory.getLogger("Route.deleteReports")
        val userId = call.parameters[USER_ID_PARAM]
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")

        userValidator.verifyUser(call = call, logger = logger, userId = userId, userToken = token) {
            call.respond(
                status = HttpStatusCode.OK,
                message = if (reportsRepository.deleteReports(userId!!)) {
                    ServiceResponse(NO_ERROR, REPORTS_DELETED_RESPONSE)
                } else {
                    logger.error("Returned 200 with a database error: $DATABASE_ISSUE - $REPORTS_NOT_DELETED_RESPONSE")
                    ServiceResponse(DATABASE_ISSUE, REPORTS_NOT_DELETED_RESPONSE)
                }
            )
        }
    }

    delete("/reports/mail") {
        val logger = LoggerFactory.getLogger("Route.deleteReportsByMail")
        val userEmail = call.receive<EmailRequest>()
        userValidator.getFirebaseUserByMail(call = call, logger = logger, email = userEmail.email) { userId ->
            call.respond(
                status = HttpStatusCode.OK,
                message = if (reportsRepository.deleteReports(userId)) {
                    userRepository.removeUser(userId)
                    true
                } else {
                    logger.error("Returned 200 with error removing: $DATABASE_ISSUE")
                    false
                }
            )
        }
    }

}
