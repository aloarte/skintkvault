package com.skintker.routes.user

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants
import com.skintker.TestConstants.userEmail
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.model.responses.ServiceResponse
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.slot
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class WipeUserTest : RoutesKoinTest() {

    @Test
    fun `test wipe user by mail success`() = testApplication {
        val client = configureClient()
        mockGetFirebaseUserByMail(true)
        coEvery { mockedReportsRepository.deleteReports(userId) } returns true
        coEvery { mockedUserManager.removeUser(userId) } just Runs

        val response = client.delete("/user/wipe") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(TestConstants.jsonBodyDeleteMail)
        }

        verifyGetFirebaseUserByMail()
        coVerify { mockedReportsRepository.deleteReports(userId) }
        coVerify { mockedUserManager.removeUser(userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("true",response.bodyAsText())
    }

    @Test
    fun `test wipe user by mail error`() = testApplication {
        val client = configureClient()
        mockGetFirebaseUserByMail(true)
        coEvery { mockedReportsRepository.deleteReports(userId) } returns false

        val response = client.delete("/user/wipe") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(TestConstants.jsonBodyDeleteMail)
        }

        verifyGetFirebaseUserByMail()
        coVerify { mockedReportsRepository.deleteReports(userId) }
        coVerify(exactly = 0) { mockedUserManager.removeUser(userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("false",response.bodyAsText())
    }

    @Test
    fun `test wipe user by mail firebase error`() = testApplication {
        val client = configureClient()
        mockGetFirebaseUserByMail(false)

        client.delete("/user/wipe") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(TestConstants.jsonBodyDeleteMail)
        }

        verifyGetFirebaseUserByMail()
        coVerify(exactly = 0) { mockedReportsRepository.deleteReports(userId) }
        coVerify(exactly = 0) { mockedUserManager.removeUser(userId) }
    }

    private fun mockGetFirebaseUserByMail(successful: Boolean) {
        val userIdResult = slot<suspend (String) -> Unit>()
        coEvery {
            mockedUserManager.getFirebaseUserByMail(
                call = any(),
                logger = any(),
                email = userEmail,
                execute = capture(userIdResult)
            )
        } coAnswers {
            if (successful) userIdResult.captured(userId)
        }
    }

    private fun verifyGetFirebaseUserByMail() {
        coVerify {
            mockedUserManager.getFirebaseUserByMail(
                call = any(),
                logger = any(),
                email = userEmail,
                execute = any()
            )
        }
    }
}
