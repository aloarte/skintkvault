package com.skintker.routes.reports

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants.userId
import com.skintker.domain.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants.INVALID_USER_ID_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORTS_DELETED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORTS_NOT_DELETED_RESPONSE
import com.skintker.domain.model.responses.ServiceResponse
import io.ktor.client.request.delete
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class DeleteReportsTest : RoutesKoinTest() {

    @Test
    fun `test delete logs success`() = testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedReportsRepository.deleteReports(userId) } returns true

        val response = client.delete("/reports/$userId")

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
        coVerify { mockedReportsRepository.deleteReports(userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(NO_ERROR, REPORTS_DELETED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test delete logs failed`()= testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedReportsRepository.deleteReports(userId) } returns false

        val response = client.delete("/reports/$userId")

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
        coVerify { mockedReportsRepository.deleteReports(userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(DATABASE_ISSUE, REPORTS_NOT_DELETED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test delete logs unauthorized`() = testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns true

        val response = client.delete("/reports/$userId")

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
        assertEquals(INVALID_USER_ID_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}
