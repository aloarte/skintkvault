package com.skintker.routes.user

import com.skintker.RoutesKoinTest
import com.skintker.TestConstants.jsonBodyAddUser
import com.skintker.TestConstants.jsonBodyAddUserMalformed
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.domain.constants.ResponseConstants
import com.skintker.domain.model.UserReturnType
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

class CreateUserTest : RoutesKoinTest() {

    @Test
    fun `test put new user success`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserManager.addUser(userToken, userId) } returns UserReturnType.UserInserted

        val response = client.put("/user/fb") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyAddUser)
        }

        coVerify { mockedUserManager.addUser(userToken, userId) }
        val serviceResponse = Json.decodeFromString<UserResponse>(response.body())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(UserReturnType.UserInserted,  serviceResponse.result)
    }

    @Test
    fun `test put new user error not inserted`() = testApplication {
        val client = configureClient()
        coEvery { mockedUserManager.addUser(userToken, userId) } returns UserReturnType.InvalidToken

        val response = client.put("/user/fb") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyAddUser)
        }

        coVerify { mockedUserManager.addUser(userToken, userId) }
        assertEquals(HttpStatusCode.OK, response.status)
        val serviceResponse = Json.decodeFromString<UserResponse>(response.body())
        assertEquals(UserReturnType.InvalidToken, serviceResponse.result)

    }

    @Test
    fun `test put new user error not inserted bad params`() = testApplication {
        val client = configureClient()

        val response = client.put("/user/fb")

        coVerify(exactly = 0) { mockedUserManager.addUser(userToken, userId) }
        assertEquals(ResponseConstants.INVALID_USER_ID_OR_TOKEN_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test put new user parse error bad json`() = testApplication {
        val client = configureClient()
        mockVerifyUser(userId, userToken, true)

        val response = client.put("/user/fb") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            contentType(ContentType.Application.Json)
            setBody(jsonBodyAddUserMalformed)
        }

        coVerify(exactly = 0) { mockedUserManager.addUser(userToken, userId) }
        assertEquals(ResponseConstants.INVALID_INPUT_RESPONSE, response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

}
