package com.skintker.routes.report

import com.skintker.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.constants.ResponseCodes.NO_ERROR
import com.skintker.constants.ResponseConstants.INVALID_PARAM_RESPONSE
import com.skintker.constants.ResponseConstants.INVALID_USER_ID_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_DELETED_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_NOT_DELETED_RESPONSE
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.responses.ServiceResponse
import com.skintker.data.validators.UserInfoValidator
import com.skintker.model.LogIdValues
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import com.skintker.routes.QueryParams.LOG_DATE_PARAM
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.test.KoinTest

class DeleteReportTest:  KoinTest {

    private val mockedDatabase = mockk<ReportsRepository>()

    private val mockedUserInfoValidator = mockk<UserInfoValidator>()

    companion object {
        const val jsonBody = "{\"date\":\"2012-04-23T18:25:43.511Z\",\"irritation\":{\"overallValue\":9,\"zoneValues\":[\"a\",\"b\",\"c\"]},\"additionalData\":{\"stressLevel\":9,\"weather\":{\"humidity\":9,\"temperature\":9},\"travel\":{\"traveled\":true,\"city\":\"Madrid\"},\"alcoholLevel\":\"None\",\"beerTypes\":[\"ba\",\"bb\",\"bc\"]}}"
        const val userId =  "userId"
        const val logDate = "08-01-2023"
        val idValues = LogIdValues(logDate, userId)
    }

    private fun ApplicationTestBuilder.configureClient() = createClient {
        with(this@configureClient) {
            application {
                configureKoin()
                configureRouting(mockedDatabase,mockedUserInfoValidator)
            }
            install(ContentNegotiation) { json() }
        }
    }

    @Test
    fun `test delete report success`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserInfoValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedDatabase.deleteReport(idValues) } returns true

        val response = client.delete("/report/$userId?$LOG_DATE_PARAM=$logDate") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        coVerify { mockedUserInfoValidator.isUserIdInvalid(userId) }
        coVerify {  mockedDatabase.deleteReport(idValues) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(NO_ERROR, REPORT_DELETED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test delete report error`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserInfoValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedDatabase.deleteReport(idValues) } returns false

        val response = client.delete("/report/$userId?$LOG_DATE_PARAM=$logDate") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        coVerify { mockedUserInfoValidator.isUserIdInvalid(userId) }
        coVerify {  mockedDatabase.deleteReport(idValues) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_DELETED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test delete report error param`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserInfoValidator.isUserIdInvalid(userId) } returns false

        val response = client.delete("/report/$userId")

        coVerify { mockedUserInfoValidator.isUserIdInvalid(userId) }
        assertEquals(INVALID_PARAM_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test delete report bad user id unauthorized response`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserInfoValidator.isUserIdInvalid(userId) } returns true

        val response = client.delete("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        coVerify { mockedUserInfoValidator.isUserIdInvalid(userId) }
        assertEquals(INVALID_USER_ID_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}