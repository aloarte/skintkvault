package com.skintker

import com.skintker.TestConstantsE2E.fbUserId
import com.skintker.TestConstantsE2E.log
import com.skintker.TestConstantsE2E.log2
import com.skintker.TestConstantsE2E.reportPath
import com.skintker.TestConstantsE2E.reportsPath
import com.skintker.TestConstantsE2E.serverUrl
import com.skintker.data.dto.logs.DailyLog
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.junit.Assert
import org.spekframework.spek2.Spek
import java.nio.charset.StandardCharsets


object TestE2eReports : Spek({

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

    group("E2E spek routes /reports") {
        beforeEachTest {
            client = HttpClients.createDefault()
        }
        test("Test E2E put 2 report test") {
            val httpPut = HttpPut("${serverUrl}/${reportPath}/${fbUserId}").apply {
                setHeader("Accept", "application/json")
                setHeader("Content-Type", "application/json")
                entity = StringEntity(Json.encodeToString(log))
            }
            val httpPut2 = HttpPut("${serverUrl}/${reportPath}/${fbUserId}").apply {
                setHeader("Accept", "application/json")
                setHeader("Content-Type", "application/json")
                entity = StringEntity(Json.encodeToString(log2))
            }

            val responseAdd1 = client.execute(httpPut)
            val responseAdd2 = client.execute(httpPut2)

            Assert.assertEquals(201, responseAdd1.statusLine.statusCode.toLong())
            Assert.assertEquals(201, responseAdd2.statusLine.statusCode.toLong())
        }

        test("Test E2E /reports get all logs") {
            val httpGet = HttpGet("${serverUrl}/${reportsPath}/${fbUserId}")

            val response = client.execute(httpGet)

            Assert.assertEquals(200, response.statusLine.statusCode.toLong())
            with(
                Json.decodeFromString<GetAllReportsDto>(
                    EntityUtils.toString(
                        response.entity,
                        StandardCharsets.UTF_8
                    )
                )
            ) {
                Assert.assertEquals(2, content.count)
                Assert.assertEquals(0, statusCode)
                Assert.assertEquals(listOf(log, log2), content.logList)
            }
        }

        test("Test E2E /reports delete all logs") {
            val httpDelete = HttpDelete("${serverUrl}/${reportsPath}/${fbUserId}")
            val response = client.execute(httpDelete)
            Assert.assertEquals(200, response.statusLine.statusCode.toLong())
        }

        test("Test E2E /reports get all logs after deletion") {
            val httpGet = HttpGet("${serverUrl}/${reportsPath}/${fbUserId}")

            val response = client.execute(httpGet)

            Assert.assertEquals(200, response.statusLine.statusCode.toLong())
            with(
                Json.decodeFromString<GetAllReportsDto>(
                    EntityUtils.toString(
                        response.entity,
                        StandardCharsets.UTF_8
                    )
                )
            ) {
                Assert.assertEquals(0, content.count)
                Assert.assertEquals(0, statusCode)
                Assert.assertEquals(emptyList<DailyLog>(), content.logList)
            }
        }
    }

    afterGroup {
        println("< E2E Test cleanup")
        server.stop(1000, 10000)
    }
})