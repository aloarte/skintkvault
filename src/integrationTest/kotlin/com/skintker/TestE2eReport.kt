package com.skintker

import com.skintker.TestConstantsE2E.date
import com.skintker.TestConstantsE2E.fbUserId
import com.skintker.TestConstantsE2E.log
import com.skintker.TestConstantsE2E.reportPath
import com.skintker.TestConstantsE2E.serverUrl
import com.skintker.routes.QueryParams.LOG_DATE_PARAM
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.netty.Netty
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.junit.Assert
import org.spekframework.spek2.Spek

object TestE2eReport : Spek({

    lateinit var server: NettyApplicationEngine

    lateinit var client: CloseableHttpClient

    beforeGroup {
        println("> E2E Test configuration")
        server = embeddedServer(
            Netty,
            port = 8080,
            host = "0.0.0.0",
            module = Application::initModuleTest
        ).start(wait = false)
    }

    group("E2E spek routes /report") {
        beforeEachTest {
            client = HttpClients.createDefault()
        }
        test("Test E2E /report add new report") {
            val httpPut = HttpPut("${serverUrl}/${reportPath}/${fbUserId}").apply {
                setHeader("Accept", "application/json")
                setHeader("Content-Type", "application/json")
                entity = StringEntity(Json.encodeToString(log))
            }

            val response = client.execute(httpPut)

            Assert.assertEquals(201, response.statusLine.statusCode.toLong())
        }

        test("Test E2E /report edit log") {
            val httpPut = HttpPut("${serverUrl}/${reportPath}/${fbUserId}").apply {
                setHeader("Accept", "application/json")
                setHeader("Content-Type", "application/json")
                entity = StringEntity(Json.encodeToString(log.copy(foodList = listOf("potatoes"))))
            }

            val response = client.execute(httpPut)

            Assert.assertEquals(200, response.statusLine.statusCode.toLong())
        }

        test("Test E2E /report delete log") {
            val httpDelete = HttpDelete("${serverUrl}/${reportPath}/${fbUserId}").apply {
                uri = URIBuilder(this.uri)
                    .addParameter(LOG_DATE_PARAM, date)
                    .build()
            }
            val response = client.execute(httpDelete)
            Assert.assertEquals(200, response.statusLine.statusCode.toLong())
        }

        test("Test E2E /report add the previous deleted report") {
            val httpPut = HttpPut("${serverUrl}/${reportPath}/${fbUserId}").apply {
                setHeader("Accept", "application/json")
                setHeader("Content-Type", "application/json")
                entity = StringEntity(Json.encodeToString(log))
            }

            val response = client.execute(httpPut)

            Assert.assertEquals(201, response.statusLine.statusCode.toLong())
        }

    }

    afterGroup {
        println("< E2E Test cleanup")
        server.stop(1000, 10000)
    }
})
