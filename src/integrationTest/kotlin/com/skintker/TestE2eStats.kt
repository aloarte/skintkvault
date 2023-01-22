package com.skintker

import com.skintker.TestConstantsE2E.fbUserId
import com.skintker.TestConstantsE2E.log
import com.skintker.TestConstantsE2E.reportPath
import com.skintker.TestConstantsE2E.serverUrl
import com.skintker.TestConstantsE2E.stats
import com.skintker.TestConstantsE2E.statsPath
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.junit.Assert
import org.spekframework.spek2.Spek
import java.nio.charset.StandardCharsets

object TestE2eStats : Spek({

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

    group("E2E spek routes /stats") {
        beforeEachTest {
            client = HttpClients.createDefault()
        }
        test("Test E2E  add few report") {
            val httpPut = HttpPut("${serverUrl}/${reportPath}/${fbUserId}").apply {
                setHeader("Accept", "application/json")
                setHeader("Content-Type", "application/json")
                entity = StringEntity(Json.encodeToString(log))
            }

            val response = client.execute(httpPut)

            Assert.assertEquals(201, response.statusLine.statusCode.toLong())
        }

        test("Test E2E /stats get stats") {
            val httpGet = HttpGet("${serverUrl}/${statsPath}/${fbUserId}")

            val response = client.execute(httpGet)

            Assert.assertEquals(200, response.statusLine.statusCode.toLong())
            with(
                Json.decodeFromString<GetStatsDto>(
                    EntityUtils.toString(
                        response.entity,
                        StandardCharsets.UTF_8
                    )
                )
            ) {
                Assert.assertEquals(0, statusCode)
                        Assert.assertEquals(stats, content.stats)
            }
        }
    }

    afterGroup {
        println("< E2E Test cleanup")
        server.stop(1000, 10000)
    }
})