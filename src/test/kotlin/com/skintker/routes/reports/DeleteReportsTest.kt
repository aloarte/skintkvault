package com.skintker.routes.reports

import com.skintker.constants.ResponseConstants.REPORTS_NOT_DELETED_RESPONSE
import com.skintker.data.repository.ReportsRepository
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.client.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import io.ktor.client.statement.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.koin.test.KoinTest

class DeleteReportsTest:  KoinTest {

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
    fun testDeleteLogsSuccess() = testApplication {
        val client = configureClient()
        coEvery { mockedDatabase.deleteReports(any()) } returns true

        val response = client.delete("/reports/$token")

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun testDeleteLogsFailed() = testApplication {
        val client = configureClient()
        coEvery { mockedDatabase.deleteReports(any()) } returns false

        val response = client.delete("/reports/$token")

        assertEquals(HttpStatusCode.InternalServerError, response.status)
        assertEquals(REPORTS_NOT_DELETED_RESPONSE, response.bodyAsText())
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