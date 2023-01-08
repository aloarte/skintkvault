package com.skintker.routes.reports

import com.skintker.constants.ResponseCodes.NO_ERROR
import com.skintker.constants.ResponseConstants
import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.AlcoholLevel
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.dto.DailyLog
import com.skintker.data.dto.Irritation
import com.skintker.data.responses.LogListResponse
import com.skintker.data.responses.ServiceResponse
import com.skintker.data.validators.UserInfoValidator
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.client.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.test.KoinTest

class GetReportsTest : KoinTest {

    companion object {
        const val userId = "userId"
        val resultList = listOf(
            DailyLog(
                date = "date",
                irritation = Irritation(
                    overallValue = 3,
                    zoneValues = listOf("Wrist", "Chest")
                ),
                additionalData = AdditionalData(
                    stressLevel = 10,
                    weather = AdditionalData.Weather(humidity = 5, temperature = 5),
                    travel = AdditionalData.Travel(false, "Madrid"),
                    alcoholLevel = AlcoholLevel.FewWine
                ),
                foodList = listOf("Bread", "Milk", "Eggs")
            )
        )
    }

    private val mockedDatabase = mockk<ReportsRepository>()

    private val mockedUserInfoValidator = mockk<UserInfoValidator>()


    private fun ApplicationTestBuilder.configureClient() = createClient {
        with(this@configureClient) {
            application {
                configureKoin()
                configureRouting(mockedDatabase, mockedUserInfoValidator)
            }
            install(ContentNegotiation) { json() }
        }
    }

    @Test
    fun `test get logs empty answer`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserInfoValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedDatabase.getReports(userId) } returns emptyList()

        val response = client.get("/reports/$userId")

        coVerify { mockedUserInfoValidator.isUserIdInvalid(userId) }
        coVerify { mockedDatabase.getReports(userId) }
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(emptyList(), (serviceResponse.content as LogListResponse).logList)
    }

    @Test
    fun testGetLogsSuccessListWithLogsAnswer() = testApplication {
        val client = configureClient()
        coEvery { mockedUserInfoValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedDatabase.getReports(userId) } returns resultList

        val response = client.get("/reports/$userId")


        coVerify { mockedUserInfoValidator.isUserIdInvalid(userId) }
        coVerify { mockedDatabase.getReports(userId) }
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(resultList, (serviceResponse.content as LogListResponse).logList)
    }

    @Test
    fun `test get logs unauthorized`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserInfoValidator.isUserIdInvalid(userId) } returns true

        val response = client.get("/reports/${userId}")

        coVerify { mockedUserInfoValidator.isUserIdInvalid(userId) }
        assertEquals(ResponseConstants.INVALID_USER_ID_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}