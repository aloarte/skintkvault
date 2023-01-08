package com.skintker.routes.reports

import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.AlcoholLevel
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.dto.DailyLog
import com.skintker.data.dto.Irritation
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.client.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import io.ktor.client.call.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.test.KoinTest

class RetrieveReportsTest:  KoinTest {

    companion object {
          const val token =  "userToken"
    }

    private val mockedDatabase = mockk<ReportsRepository>()

    private fun ApplicationTestBuilder.configureClient() = createClient {
        with(this@configureClient) {
            application {
                configureKoin()
                configureRouting(mockedDatabase)
            }
            install(ContentNegotiation) { json() }
        }
    }

    @Test
    fun testGetLogsSuccessEmptyAnswer() = testApplication {
        val client = configureClient()
        coEvery { mockedDatabase.getReports(any()) } returns emptyList()

        val response = client.get("/reports/$token")

        val logsList= Json.decodeFromString<List<DailyLog>>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(emptyList(), logsList)
    }

    @Test
    fun testGetLogsSuccessListWithLogsAnswer() = testApplication {
        val client = configureClient()
        val resultList = listOf(
            DailyLog(
                date= "date",
                irritation = Irritation(
                    overallValue=3,
                    zoneValues = listOf("Wrist","Chest")
                ),
                additionalData = AdditionalData(
                    stressLevel=10,
                    weather= AdditionalData.Weather(humidity = 5, temperature = 5),
                    travel = AdditionalData.Travel(false,"Madrid"),
                    alcoholLevel = AlcoholLevel.FewWine
                ),
                foodList = listOf("Bread","Milk","Eggs"))
        )
        coEvery { mockedDatabase.getReports(any()) } returns resultList

        val response = client.get("/reports/$token")

        val logsList= Json.decodeFromString<List<DailyLog>>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(resultList, logsList)
    }

//    @Test
//    fun testGetLogsUnauthorized() = testApplication {
//        val client = configureClient()
//
//        val response = client.put("/reports/invalidToken")
//
//        assertEquals(INVALID_TOKEN_RESPONSE, response.bodyAsText())
//        assertEquals(HttpStatusCode.Unauthorized, response.status)
//    }
}