package com.skintker.routes.stats

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants.stats
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.data.IRRITATION_THRESHOLD
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.domain.model.responses.StatsResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetStatsTest : RoutesKoinTest() {

    companion object{
        const val threshold = 2
    }

    @Test
    fun `test get stats success default threshold parameter`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedStatsRepository.calculateUserStats(userId,IRRITATION_THRESHOLD) } returns stats

        val response = client.get("/stats/$userId"){
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        verifyVerifyUser(userId, userToken)
        coVerify { mockedStatsRepository.calculateUserStats(userId,IRRITATION_THRESHOLD) }
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(stats, (serviceResponse.content as StatsResponse).stats)
    }

    @Test
    fun `test get stats success threshold parameter`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedStatsRepository.calculateUserStats(userId,threshold) } returns stats

        val response = client.get("/stats/$userId?threshold=$threshold"){
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        verifyVerifyUser(userId, userToken)
        coVerify { mockedStatsRepository.calculateUserStats(userId,threshold) }
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(stats, (serviceResponse.content as StatsResponse).stats)
    }

    @Test
    fun `test get stats unauthorized bad token or userId`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, false)

        client.get("/stats/${userId}"){
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        verifyVerifyUser(userId, userToken)
    }

    @Test
    fun `test get stats unauthorized bad token not given`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, null, false)

        client.get("/stats/${userId}")

        verifyVerifyUser(userId, null)
    }

}
