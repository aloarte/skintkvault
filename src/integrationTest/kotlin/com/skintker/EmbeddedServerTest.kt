package com.skintker

import com.skintker.TestConstants.fbUserId
import com.skintker.TestConstants.log
import com.skintker.TestConstants.reportPath
import com.skintker.TestConstants.reportsPath
import com.skintker.TestConstants.serverUrl
import com.skintker.dto.GetAllReportsDto
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
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

object MyTest: Spek({

    lateinit var server: NettyApplicationEngine

    lateinit var client: CloseableHttpClient

    beforeGroup {
        println("> E2E Test configuration")
        server = embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::initModuleTest).start(wait = false)
        client = HttpClients.createDefault()
    }

    group("E2E spek routes") {


        test("E2E put 2 report test") {
            val httpPut = HttpPut("${serverUrl}/${reportPath}/${fbUserId}").apply {
                setHeader("Accept", "application/json")
                setHeader("Content-Type", "application/json")
                entity = StringEntity(Json.encodeToString(log))
            }

            val response = client.execute(httpPut)
            Assert.assertEquals(201, response.statusLine.statusCode.toLong())

        }

        group("E2E routes group tests"){
            test("some test") {
                println("some test")
            }

            test("another test") {
                println("another test")
            }
        }

//        test("Test E2E delete all logs") {
//            val httpDelete = HttpDelete("${serverUrl}/${reportsPath}/${fbUserId}")
//
//            val response = client.execute(httpDelete)
//
//            Assert.assertEquals(200, response.statusLine.statusCode.toLong())
//
//        }

        test("Test E2E get all logs after delete") {
            val httpGet = HttpGet("${serverUrl}/${reportsPath}/${fbUserId}")

            val response = client.execute(httpGet)

            Assert.assertEquals(200, response.statusLine.statusCode.toLong())
            with(Json.decodeFromString<GetAllReportsDto>(EntityUtils.toString(response.entity, StandardCharsets.UTF_8))){
                Assert.assertEquals(listOf(log), content.logList)
                Assert.assertEquals(1, content.count)
                Assert.assertEquals(0, statusCode)
            }


        }
    }

    afterGroup {
        println("< E2E Test cleanup")
        // clean up after this class, leave nothing dirty behind
        server.stop(1000, 10000)
    }
})