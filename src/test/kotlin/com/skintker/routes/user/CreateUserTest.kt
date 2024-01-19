package com.skintker.routes.user

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.model.responses.ServiceResponse
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class CreateUserTest : RoutesKoinTest() {

    @Test
    fun `test put new user success`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserManager.addUser(userToken, userId) } returns true

        val response = client.put("/user/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        coVerify { mockedUserManager.addUser(userToken, userId) }
        assertEquals(HttpStatusCode.Created, response.status)
        val expectedResponse = ServiceResponse(
            NO_ERROR,
            ResponseConstants.USER_CREATED_RESPONSE
        )
        assertEquals(expectedResponse, Json.decodeFromString(response.bodyAsText()))
    }

    @Test
    fun `test put new user error not inserted`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserManager.addUser(userToken, userId) } returns false

        val response = client.put("/user/$userId") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }

        coVerify { mockedUserManager.addUser(userToken, userId) }
        assertEquals(ResponseConstants.USER_NOT_CREATED_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun `test put new user error not inserted bad params`() = testApplication {
        val client = configureClient()

        val response = client.put("/user/$userId")

        coVerify(exactly = 0) { mockedUserManager.addUser(userToken, userId) }
        assertEquals(ResponseConstants.INVALID_USER_ID_OR_TOKEN_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
