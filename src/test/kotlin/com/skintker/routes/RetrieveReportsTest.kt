package com.skintker.routes

import com.skintker.manager.DatabaseManager
import com.skintker.model.DailyLog
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.client.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import io.ktor.client.call.*
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.test.KoinTest

class RetrieveReportsTest:  KoinTest {

    companion object {
          const val token =  "userToken"
    }

    val mockedDatabase = mockk<DatabaseManager>()

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
        every { mockedDatabase.getReports(any()) } returns emptyList()

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
                irritation = DailyLog.Irritation(
                    overallValue=3,
                    zoneValues = listOf("Wrist","Chest")
                ),
                additionalData = DailyLog.AdditionalData(
                    stressLevel=10,
                    weather= DailyLog.AdditionalData.Weather(humidity = 5, temperature = 5),
                    travel = DailyLog.AdditionalData.Travel(false,"Madrid"),
                    alcoholLevel = DailyLog.AlcoholLevel.FewWine
                ),
                foodList = listOf("Bread","Milk","Eggs"))
        )
        every { mockedDatabase.getReports(any()) } returns resultList

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