package com.skintker.routes.user

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants.jsonBodyAddUser
import com.skintker.TestConstants.jsonBodyAddUserMalformed
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.domain.constants.ResponseCodes.NO_ERROR
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.model.UserResult
import com.skintker.domain.model.responses.ServiceResponse
import com.skintker.domain.model.responses.UserResponse
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CreateUserTest : RoutesKoinTest() {

    @Test
    fun `test put new user success`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserManager.addUser(userToken, userId) } returns UserResult.UserInsert(true)

        val response = client.put("/user") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyAddUser)
        }

        coVerify { mockedUserManager.addUser(userToken, userId) }
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(UserResult.UserInsert(true), (serviceResponse.content as UserResponse).result)
    }

    @Test
    fun `test put new user error not inserted`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserManager.addUser(userToken, userId) } returns UserResult.InvalidToken

        val response = client.put("/user") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyAddUser)
        }

        coVerify{ mockedUserManager.addUser(userToken, userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        val serviceResponse = Json.decodeFromString<ServiceResponse>(response.body())
        assertEquals(NO_ERROR, serviceResponse.statusCode)
        assertNull(serviceResponse.statusMessage)
        assertEquals(UserResult.InvalidToken, (serviceResponse.content as UserResponse).result)

    }

    @Test
    fun `test put new user error not inserted bad params`() = testApplication {
        val client = configureClient()

        val response = client.put("/user")

        coVerify(exactly = 0) { mockedUserManager.addUser(userToken, userId) }
        assertEquals(ResponseConstants.INVALID_USER_ID_OR_TOKEN_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test put new user parse error bad json`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)

        val response = client.put("/user") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyAddUserMalformed)
        }

        coVerify(exactly = 0) { mockedUserManager.addUser(userToken, userId) }
        assertEquals(ResponseConstants.INVALID_INPUT_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

}
