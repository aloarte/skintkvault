package com.skintker

import com.skintker.data.repository.ReportsRepository
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import org.koin.test.KoinTest
import org.koin.test.inject

class ApplicationTest: KoinTest {

    private val mockedDatabase by inject<ReportsRepository>()

    @Test
    fun testRoot() = testApplication {
        application {
            configureKoin()
            configureRouting(mockedDatabase)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

}