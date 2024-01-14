package com.skintker.routes.reports

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants
import com.skintker.TestConstants.jsonBodyDeleteMail
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.domain.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants.REPORTS_DELETED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORTS_NOT_DELETED_RESPONSE
import com.skintker.domain.model.responses.ServiceResponse
import io.ktor.client.request.delete
import io.ktor.client.request.header
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
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class DeleteReportsTest : RoutesKoinTest() {

    @Test
    fun `test delete logs success`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedReportsRepository.deleteReports(userId) } returns true

        val response = client.delete("/reports/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        verifyVerifyUser(userId, userToken)
        coVerify { mockedReportsRepository.deleteReports(userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(NO_ERROR, REPORTS_DELETED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test delete logs failed`()= testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedReportsRepository.deleteReports(userId) } returns false

        val response = client.delete("/reports/$userId"){
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        verifyVerifyUser(userId, userToken)
        coVerify { mockedReportsRepository.deleteReports(userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(DATABASE_ISSUE, REPORTS_NOT_DELETED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test delete logs unauthorized bad token or userId`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, false)

        client.delete("/reports/$userId"){
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        verifyVerifyUser(userId, userToken)
        //The return of the unauthorized part is handled by the UserValidator
    }

    @Test
    fun `test delete logs unauthorized token not given`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, null, false)

        client.delete("/reports/$userId")

        verifyVerifyUser(userId, null)
        //The return of the unauthorized part is handled by the UserValidator
    }

    @Test
    fun `test wipe data by mail success`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedUserValidator.getFirebaseUserByMail(any(),any(),"email@test.com",any()) } just Runs

        val response = client.delete("/reports/mail"){
            contentType(ContentType.Application.Json)
            setBody(jsonBodyDeleteMail)
        }

        verifyVerifyUser(userId, userToken)
        coVerify { mockedReportsRepository.deleteReports(userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(NO_ERROR, REPORTS_DELETED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

}
