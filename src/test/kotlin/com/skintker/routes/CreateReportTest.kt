package com.skintker.routes

import com.skintker.constants.ResponseConstants.INVALID_INPUT_RESPONSE
import com.skintker.constants.ResponseConstants.REPORT_STORED_RESPONSE
import com.skintker.manager.DatabaseManager
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import org.koin.test.KoinTest
import org.koin.test.inject

class CreateReportTest:  KoinTest {

    private val mockedDatabase : DatabaseManager by inject()

    companion object {
        const val jsonBody = "{\"date\":\"2012-04-23T18:25:43.511Z\",\"irritation\":{\"overallValue\":9,\"zoneValues\":[\"a\",\"b\",\"c\"]},\"additionalData\":{\"stressLevel\":9,\"weather\":{\"humidity\":9,\"temperature\":9},\"travel\":{\"traveled\":true,\"city\":\"Madrid\"},\"alcoholLevel\":\"None\",\"beerTypes\":[\"ba\",\"bb\",\"bc\"]}}"
        const val badJson = "{\"dte\":\"2012-04-23T18:25:43.511Z\",\"irritation\":{\"overallValue\":9,\"zoneValues\":[\"a\",\"b\",\"c\"]},\"additionalData\":{\"stressLevel\":9,\"weather\":{\"humidity\":9,\"temperature\":9},\"travel\":{\"traveled\":true,\"city\":\"Madrid\"},\"alcoholLevel\":\"None\",\"beerTypes\":[\"ba\",\"bb\",\"bc\"]}}"
        const val differentJson = "\"date\":\"2012-04-23T18:25:43.511Z\",\"irritation\":{\"overallValue\":9,\"zoneValues\":[\"a\",\"b\",\"c\"]},\"additionalData\":{\"stressLevel\":9,\"weather\":{\"humidity\":9,\"temperature\":9},\"travel\":{\"traveled\":true,\"city\":\"Madrid\"},\"alcoholLevel\":\"None\",\"beerTypes\":[\"ba\",\"bb\",\"bc\"]}}"
        const val token =  "userToken"
    }

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
    fun testPutLogSuccess() = testApplication {
        val client = configureClient()

        val response = client.put("/report/$token") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        assertEquals(REPORT_STORED_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.Created, response.status)
    }


    @Test
    fun testPutLogParseErrorDifferentJson() = testApplication {
        val client = configureClient()

        val response = client.put("/report/$token") {
            contentType(ContentType.Application.Json)
            setBody(differentJson)
        }

        assertEquals(INVALID_INPUT_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testPutLogParseErrorBadJson() = testApplication {
        val client = configureClient()

        val response = client.put("/report/$token") {
            contentType(ContentType.Application.Json)
            setBody(badJson)
        }

        assertEquals(INVALID_INPUT_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

//    @Test
//    fun testPutLogUnauthorized() = testApplication {
//        val client = configureClient()
//
//        val response = client.put("/report/") {
//            contentType(ContentType.Application.Json)
//            setBody(jsonBody)
//        }
//
//        assertEquals(INVALID_TOKEN_RESPONSE, response.bodyAsText())
//        assertEquals(HttpStatusCode.Unauthorized, response.status)
//    }
}