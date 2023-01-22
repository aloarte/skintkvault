package com.skintker.routes.stats

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants.stats
import com.skintker.TestConstants.userId
import com.skintker.data.IRRITATION_THRESHOLD
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.domain.model.responses.StatsResponse
import io.ktor.http.*
import io.ktor.client.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class GetStatsTest : RoutesKoinTest() {

    companion object{
        const val threshold = 2
    }

    @Test
    fun `test get stats success default threshold parameter`() = testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedStatsRepository.calculateUserStats(userId,IRRITATION_THRESHOLD) } returns stats

        val response = client.get("/stats/$userId")

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
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
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns false
        coEvery { mockedStatsRepository.calculateUserStats(userId,threshold) } returns stats

        val response = client.get("/stats/$userId?threshold=$threshold")

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
        coVerify { mockedStatsRepository.calculateUserStats(userId,threshold) }
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(stats, (serviceResponse.content as StatsResponse).stats)
    }

    @Test
    fun `test get stats unauthorized`() = testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.isUserIdInvalid(userId) } returns true

        val response = client.get("/stats/${userId}")

        coVerify { mockedInputValidator.isUserIdInvalid(userId) }
        assertEquals(ResponseConstants.INVALID_USER_ID_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}
