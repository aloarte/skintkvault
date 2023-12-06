package com.skintker.routes.report

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants
import com.skintker.TestConstants.idValues
import com.skintker.TestConstants.jsonBodyLog
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.domain.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.domain.constants.ResponseConstants.INVALID_PARAM_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_NOT_DELETED_RESPONSE
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.routes.QueryParams.LOG_DATE_PARAM
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class DeleteReportTest : RoutesKoinTest() {

    @Test
    fun `test delete report error`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedReportsRepository.deleteReport(idValues) } returns false

        val response = client.delete("/report/$userId?$LOG_DATE_PARAM=${TestConstants.date}") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyLog)
        }

        verifyVerifyUser(userId, userToken)
        coVerify { mockedReportsRepository.deleteReport(idValues) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_DELETED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test delete report error param`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)

        val response = client.delete("/report/$userId"){
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        verifyVerifyUser(userId, userToken)
        assertEquals(INVALID_PARAM_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test delete report bad user id unauthorized bad token or userId`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, false)

        client.delete("/report/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyLog)
        }

        verifyVerifyUser(userId, userToken)
    }

    @Test
    fun `test delete report bad user id unauthorized token not given`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, null, false)

        client.delete("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(jsonBodyLog)
        }

        verifyVerifyUser(userId, null)
    }

}
