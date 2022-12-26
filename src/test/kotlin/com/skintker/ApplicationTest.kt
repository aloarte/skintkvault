package com.skintker

import com.skintker.manager.DatabaseManager
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.skintker.plugins.*
import org.koin.test.KoinTest
import org.koin.test.inject

class ApplicationTest: KoinTest {

    private val mockedDatabase by inject<DatabaseManager>()

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