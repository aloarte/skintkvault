package com.skintker.routes.report

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants.jsonBodyLog
import com.skintker.TestConstants.jsonDeserializedLog
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.domain.constants.ResponseCodes.DATABASE_ISSUE
import com.skintker.domain.constants.ResponseCodes.INVALID_INPUT
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants.INVALID_INPUT_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_EDITED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_NOT_EDITED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_NOT_STORED_RESPONSE
import com.skintker.domain.constants.ResponseConstants.REPORT_STORED_RESPONSE
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.domain.model.SaveReportStatus
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class CreateReportTest : RoutesKoinTest() {

    companion object {
        const val onlyDateJsonBody = "{\"date\":\"2012-04-23T18:25:43.511Z\"}"
        const val badJson =
            "{\n" +
                    "  \"dte\": \"2012-04-23T18:25:43.511Z\",\n" +
                    "  \"irritation\": {\n" +
                    "    \"overallValue\": 9,\n" +
                    "    \"zoneValues\": [\n" +
                    "      \"a\",\n" +
                    "      \"b\",\n" +
                    "      \"c\"\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"additionalData\": {\n" +
                    "    \"stressLevel\": 9,\n" +
                    "    \"weather\": {\n" +
                    "      \"humidity\": 9,\n" +
                    "      \"temperature\": 9\n" +
                    "    },\n" +
                    "    \"travel\": {\n" +
                    "      \"traveled\": true,\n" +
                    "      \"city\": \"Madrid\"\n" +
                    "    },\n" +
                    "    \"alcoholLevel\": \"None\",\n" +
                    "    \"beerTypes\": [\n" +
                    "      \"ba\",\n" +
                    "      \"bb\",\n" +
                    "      \"bc\"\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}"
        const val differentJson =
            "  \"date\": \"2012-04-23T18:25:43.511Z\",\n" +
                    "  \"irritation\": {\n" +
                    "    \"overallValue\": 9,\n" +
                    "    \"zoneValues\": [\n" +
                    "      \"a\",\n" +
                    "      \"b\",\n" +
                    "      \"c\"\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"additionalData\": {\n" +
                    "    \"stressLevel\": 9,\n" +
                    "    \"weather\": {\n" +
                    "      \"humidity\": 9,\n" +
                    "      \"temperature\": 9\n" +
                    "    },\n" +
                    "    \"travel\": {\n" +
                    "      \"traveled\": true,\n" +
                    "      \"city\": \"Madrid\"\n" +
                    "    },\n" +
                    "    \"alcoholLevel\": \"None\",\n" +
                    "    \"beerTypes\": [\n" +
                    "      \"ba\",\n" +
                    "      \"bb\",\n" +
                    "      \"bc\"\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}"
    }


    @Test
    fun `test put new report success status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.GoodInput)
        coEvery { mockedReportsRepository.saveReport(userId, jsonDeserializedLog) } returns SaveReportStatus.Saved

        val response = client.put("/report/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyLog)
        }

        mockInputValidatorVerify(TestDataInput.GoodInput)
        coVerify { mockedReportsRepository.saveReport(userId, jsonDeserializedLog) }
        assertEquals(HttpStatusCode.Created, response.status)
        val expectedResponse = ServiceResponse(NO_ERROR, REPORT_STORED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }


    @Test
    fun `test put new report fail status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.GoodInput)
        coEvery {
            mockedReportsRepository.saveReport(userId, jsonDeserializedLog)
        } returns SaveReportStatus.SavingFailed

        val response = client.put("/report/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyLog)
        }

        mockInputValidatorVerify(TestDataInput.GoodInput)
        coVerify { mockedReportsRepository.saveReport(userId, jsonDeserializedLog) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_STORED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test put edited report success status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.GoodInput)
        coEvery { mockedReportsRepository.saveReport(userId, jsonDeserializedLog) } returns SaveReportStatus.Edited

        val response = client.put("/report/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyLog)
        }

        mockInputValidatorVerify(TestDataInput.GoodInput)
        coVerify { mockedReportsRepository.saveReport(userId, jsonDeserializedLog) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(NO_ERROR, REPORT_EDITED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test put edited report fail status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.GoodInput)
        coEvery {
            mockedReportsRepository.saveReport(userId, jsonDeserializedLog)
        } returns SaveReportStatus.EditingFailed

        val response = client.put("/report/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyLog)
        }

        mockInputValidatorVerify(TestDataInput.GoodInput)
        coVerify { mockedReportsRepository.saveReport(userId, jsonDeserializedLog) }
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(DATABASE_ISSUE, REPORT_NOT_EDITED_RESPONSE)
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test put report bad input status`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.InvalidLog)

        val response = client.put("/report/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyLog)
        }

        mockInputValidatorVerify(TestDataInput.InvalidLog)
        assertEquals(HttpStatusCode.OK, response.status)
        val expectedResponse = ServiceResponse(INVALID_INPUT, "Invalid log")
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test put report bad user id unauthorized bad token or userId`() = testApplication {
        val client = configureClient()
        mockInputValidatorEvery(TestDataInput.InvalidUser)

        client.put("/report/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(onlyDateJsonBody)
        }

        mockInputValidatorVerify(TestDataInput.InvalidUser)
        //The return of the unauthorized part is handled by the UserValidator

    }

    @Test
    fun `test put report bad user id unauthorized token not given`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, null, false)

        client.put("/report/$userId") {
            contentType(ContentType.Application.Json)
            setBody(onlyDateJsonBody)
        }

        verifyVerifyUser(userId, null)
        //The return of the unauthorized part is handled by the UserValidator

    }

    @Test
    fun `test put report parse error different json`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)

        val response = client.put("/report/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(differentJson)
        }

        verifyVerifyUser(userId, userToken)
        assertEquals(INVALID_INPUT_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test put report parse error bad json`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)

        val response = client.put("/report/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(badJson)
        }

        verifyVerifyUser(userId, userToken)
        assertEquals(INVALID_INPUT_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }


    private enum class TestDataInput {
        GoodInput, InvalidLog, InvalidUser
    }

    private fun mockInputValidatorEvery(input: TestDataInput) {
        when (input) {
            TestDataInput.GoodInput -> {
                mockVerifyUser(userId, userToken, true)
                coEvery { mockedInputValidator.isLogInvalid(any()) } returns null
            }

            TestDataInput.InvalidLog -> {
                mockVerifyUser(userId, userToken, true)
                coEvery { mockedInputValidator.isLogInvalid(any()) } returns "Invalid log"
            }

            TestDataInput.InvalidUser -> {
                mockVerifyUser(userId, userToken, false)
            }
        }
    }

    private fun mockInputValidatorVerify(input: TestDataInput) {
        when (input) {
            TestDataInput.InvalidUser -> {
                verifyVerifyUser(userId, userToken)
            }

            else -> {
                verifyVerifyUser(userId, userToken)
                coVerify { mockedInputValidator.isLogInvalid(any()) }
            }
        }
    }
}
