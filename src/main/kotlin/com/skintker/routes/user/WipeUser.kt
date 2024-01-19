package com.skintker.routes.user

import com.skintker.data.model.EmailRequest
import com.skintker.domain.UserManager
import com.skintker.domain.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.domain.repository.ReportsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import org.slf4j.LoggerFactory

fun Route.wipeUserByMail(reportsRepository: ReportsRepository, userManager: UserManager) {
    delete("/user/wipe") {
        val logger = LoggerFactory.getLogger("Route.wipeUserByMail")
        val userEmail = call.receive<EmailRequest>()
        userManager.getFirebaseUserByMail(call = call, logger = logger, email = userEmail.email) { userId ->
            call.respond(
                status = HttpStatusCode.OK,
                message = if (reportsRepository.deleteReports(userId)) {
                    userManager.removeUser(userId)
                    true
                } else {
                    logger.error("Returned 200 with error removing: $DATABASE_ISSUE")
                    false
                }
            )
        }
    }
}
