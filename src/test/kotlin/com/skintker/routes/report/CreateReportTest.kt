package com.skintker.routes.report

import com.skintker.domain.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.domain.constants.ResponseCodes.INVALID_INPUT
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants.INVALID_INPUT_RESPONSE
import com.skintker.domain.constants.ResponseConstants.INVALID_USER_ID_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_EDITED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_NOT_EDITED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_NOT_STORED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_STORED_RESPONSE
import com.skintker.domain.repository.ReportsRepository
import com.skintker.data.dto.DailyLog
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.data.validators.InputValidator
import com.skintker.domain.model.SaveReportStatus
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.test.KoinTest

class CreateReportTest : KoinTest {

    private val mockedDatabase = mockk<ReportsRepository>()

    private val mockedInputValidator = mockk<InputValidator>()

    companion object {
        const val jsonBody =
            "{\"date\":\"2012-05-41\",\"irritation\":{\"overallValue\":7,\"zoneValues\":[\"wrist\"]},\"additionalData\":{\"stressLevel\":10,\"weather\":{\"humidity\":7,\"temperature\":1},\"travel\":{\"traveled\":false,\"city\":\"Madrid\"},\"alcoholLevel\":\"FewWine\",\"beerTypes\":[\"Ale\"]},\"foodList\":[\"food1\",\"food2\"]}"
        const val onlyDateJsonBody = "{\"date\":\"2012-04-23T18:25:43.511Z\"}"
        const val badJson =
            "{\"dte\":\"2012-04-23T18:25:43.511Z\",\"irritation\":{\"overallValue\":9,\"zoneValues\":[\"a\",\"b\",\"c\"]},\"additionalData\":{\"stressLevel\":9,\"weather\":{\"humidity\":9,\"temperature\":9},\"travel\":{\"traveled\":true,\"city\":\"Madrid\"},\"alcoholLevel\":\"None\",\"beerTypes\":[\"ba\",\"bb\",\"bc\"]}}"
        const val differentJson =
            "\"date\":\"2012-04-23T18:25:43.511Z\",\"irritation\":{\"overallValue\":9,\"zoneValues\":[\"a\",\"b\",\"c\"]},\"additionalData\":{\"stressLevel\":9,\"weather\":{\"humidity\":9,\"temperature\":9},\"travel\":{\"traveled\":true,\"city\":\"Madrid\"},\"alcoholLevel\":\"None\",\"beerTypes\":[\"ba\",\"bb\",\"bc\"]}}"
        const val userId = "userId"
        val jsonDeserialized = Json.decodeFromString<DailyLog>(jsonBody)
    }

    private fun ApplicationTestBuilder.configureClient() = createClient {
        with(this@configureClient) {
            application {
                configureKoin()
                configureRouting(mockedDatabase, mockedInputValidator)
            }
            install(ContentNegotiation) { json() }
        }
    }

    @Test
    fun `test put new report success status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.GoodInput)
        coEvery { mockedDatabase.saveReport(userId, jsonDeserialized) } returns SaveReportStatus.Saved

        val response = client.put("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        mockInputValidatorVerify(TestDataInput.GoodInput)
        coVerify { mockedDatabase.saveReport(userId, jsonDeserialized) }
        assertEquals(HttpStatusCode.Created, response.status)
        val expectedResponse = ServiceResponse(NO_ERROR, REPORT_STORED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }


    @Test
    fun `test put new report fail status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.GoodInput)
        coEvery { mockedDatabase.saveReport(userId, jsonDeserialized) } returns SaveReportStatus.SavingFailed

        val response = client.put("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        mockInputValidatorVerify(TestDataInput.GoodInput)
        coVerify { mockedDatabase.saveReport(userId, jsonDeserialized) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_STORED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test put edited report success status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.GoodInput)
        coEvery { mockedDatabase.saveReport(userId, jsonDeserialized) } returns SaveReportStatus.Edited

        val response = client.put("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        mockInputValidatorVerify(TestDataInput.GoodInput)
        coVerify { mockedDatabase.saveReport(userId, jsonDeserialized) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(NO_ERROR, REPORT_EDITED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test put edited report fail status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.GoodInput)
        coEvery { mockedDatabase.saveReport(userId, jsonDeserialized) } returns SaveReportStatus.EditingFailed

        val response = client.put("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        mockInputValidatorVerify(TestDataInput.GoodInput)
        coVerify { mockedDatabase.saveReport(userId, jsonDeserialized) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_EDITED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test put report bad input status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.InvalidLog)

        val response = client.put("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        mockInputValidatorVerify(TestDataInput.InvalidLog)
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(INVALID_INPUT, "Invalid log")
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }


    @Test
    fun `test put report bad user id unauthorized response`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.InvalidUser)

        val response = client.put("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(onlyDateJsonBody)
        }

        mockInputValidatorVerify(TestDataInput.InvalidUser)
        assertEquals(INVALID_USER_ID_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }


    @Test
    fun `test put report parse error different json`() = testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns false

        val response = client.put("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(differentJson)
        }

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
        assertEquals(INVALID_INPUT_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test put report parse error bad json`() = testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns false

        val response = client.put("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(badJson)
        }

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
        assertEquals(INVALID_INPUT_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }



    private enum class TestDataInput {
        GoodInput, InvalidLog, InvalidUser
    }

    private fun mockInputValidatorEvery(input: TestDataInput) {
        when (input) {
            TestDataInput.GoodInput -> {
                coEvery { mockedInputValidator.isUserIdInvalid(any()) } returns false
                coEvery { mockedInputValidator.isLogInvalid(any()) } returns null
            }

            TestDataInput.InvalidLog -> {
                coEvery { mockedInputValidator.isUserIdInvalid(any()) } returns false
                coEvery { mockedInputValidator.isLogInvalid(any()) } returns "Invalid log"
            }

            TestDataInput.InvalidUser -> {
                coEvery { mockedInputValidator.isUserIdInvalid(any()) } returns true
            }
        }
    }

    private fun mockInputValidatorVerify(input: TestDataInput) {
        when (input) {
            TestDataInput.InvalidUser -> {
                coVerify { mockedInputValidator.isUserIdInvalid(any()) }
            }
            else -> {
                coVerify { mockedInputValidator.isUserIdInvalid(any()) }
                coVerify { mockedInputValidator.isLogInvalid(any()) }
            }


        }

    }
}