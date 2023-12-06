package com.skintker.routes.reports

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants.logList
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.model.responses.LogListResponse
import com.skintker.domain.model.responses.ServiceResponse
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetReportsTest : RoutesKoinTest() {

    companion object{
        private const val offset = "2"
        private const val limit = "4"
    }

    @Test
    fun `test get logs empty answer no pagination`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedReportsRepository.getReports(userId) } returns emptyList()

        val response = client.get("/reports/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        verifyVerifyUser(userId, userToken)
        coVerify { mockedReportsRepository.getReports(userId) }
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(emptyList(), (serviceResponse.content as LogListResponse).logList)
    }

    @Test
    fun `test get logs success answer no pagination`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedReportsRepository.getReports(userId) } returns logList

        val response = client.get("/reports/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        verifyVerifyUser(userId, userToken)
        coVerify { mockedReportsRepository.getReports(userId) }
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(logList, (serviceResponse.content as LogListResponse).logList)
        assertEquals(logList.size, (serviceResponse.content as LogListResponse).count)
    }

    @Test
    fun `test get logs success answer with valid pagination`() = testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.arePaginationIndexesInvalid(limit, offset, logList.size) } returns false
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedReportsRepository.getReports(userId) } returns logList
        coEvery { mockedPaginationManager.getPageFromLogs(limit,offset, logList) } returns logList

        val response = client.get("/reports/$userId?offset=$offset&limit=$limit") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        coVerify { mockedInputValidator.arePaginationIndexesInvalid(limit, offset, logList.size) }
        verifyVerifyUser(userId, userToken)
        coVerify { mockedReportsRepository.getReports(userId) }
        coVerify { mockedPaginationManager.getPageFromLogs(limit,offset, logList) }
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(logList, (serviceResponse.content as LogListResponse).logList)
        assertEquals(logList.size, (serviceResponse.content as LogListResponse).count)
    }

    @Test
    fun `test get logs success answer with invalid pagination`() = testApplication {
        val client = configureClient()
        coEvery { mockedInputValidator.arePaginationIndexesInvalid(limit, offset, logList.size) } returns true
        mockVerifyUser(userId, userToken, true)
        coEvery { mockedReportsRepository.getReports(userId) } returns logList

        val response = client.get("/reports/$userId?offset=$offset&limit=$limit") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        coVerify { mockedInputValidator.arePaginationIndexesInvalid(limit, offset, logList.size) }
        verifyVerifyUser(userId, userToken)
        coVerify { mockedReportsRepository.getReports(userId) }
        assertEquals(ResponseConstants.BAD_INPUT_DATA, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test get logs unauthorized bad token or userId`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, false)

        client.get("/reports/${userId}"){
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }
        verifyVerifyUser(userId, userToken)
        //The return of the unauthorized part is handled by the UserValidator
    }

    @Test
    fun `test get logs unauthorized token not given`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, null, false)

        client.get("/reports/${userId}")

        verifyVerifyUser(userId, null)
        //The return of the unauthorized part is handled by the UserValidator
    }

}
