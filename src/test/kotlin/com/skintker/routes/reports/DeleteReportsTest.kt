package com.skintker.routes.reports

import com.skintker.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.constants.ResponseCodes.NO_ERROR
import com.skintker.constants.ResponseConstants.INVALID_USER_ID_RESPONSE
import com.skintker.constants.ResponseConstants.REPORTS_DELETED_RESPONSE
import com.skintker.constants.ResponseConstants.REPORTS_NOT_DELETED_RESPONSE
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.responses.ServiceResponse
import com.skintker.data.validators.InputValidator
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.client.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import io.ktor.client.statement.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.test.KoinTest

class DeleteReportsTest:  KoinTest {

    companion object {
          const val userId =  "userId"
    }

    private val mockedDatabase = mockk<ReportsRepository>()

    private val mockedInputValidator = mockk<InputValidator>()

    private fun ApplicationTestBuilder.configureClient() = createClient {
        with(this@configureClient) {
            application {
                configureKoin()
                configureRouting(mockedDatabase,mockedInputValidator)
            }
            install(ContentNegotiation) { json() }
        }
    }

    @Test
    fun `test delete logs success`() = testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedDatabase.deleteReports(userId) } returns true

        val response = client.delete("/reports/$userId")

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
        coVerify { mockedDatabase.deleteReports(userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(NO_ERROR, REPORTS_DELETED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test delete logs failed`()= testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedDatabase.deleteReports(userId) } returns false

        val response = client.delete("/reports/$userId")

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
        coVerify { mockedDatabase.deleteReports(userId) }
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