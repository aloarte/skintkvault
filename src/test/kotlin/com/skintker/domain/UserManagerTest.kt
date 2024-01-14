package com.skintker.domain

import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.domain.constants.ResponseConstants.INVALID_USER_ID_RESPONSE
import com.skintker.domain.constants.ResponseConstants.INVALID_USER_TOKEN_RESPONSE
import com.skintker.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import io.ktor.http.HttpStatusCode
import io.mockk.Runs
import org.junit.Before
import org.junit.Ignore
import org.slf4j.Logger
import kotlin.test.Test
import io.mockk.mockk
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.verify

class UserManagerTest {

    private val repository = mockk<UserRepository>()

    private val call = mockk<ApplicationCall>()

    private val logger = mockk<Logger>()

    private val callback = mockk<suspend () -> Unit>()

    private lateinit var userManager: UserManager

    @Before
    fun setup() {
        userManager = UserManager(repository)
    }

    @Test
    fun `test verify user valid user and token`() {
        coEvery { repository.isTokenValid(userToken) } returns true
        coEvery { repository.isUserValid(userId) } returns true
        coEvery { callback.invoke() } just Runs

        runBlocking {
            userManager.verifyUser(call, logger, userToken, userId, callback)
        }

        coVerify { repository.isTokenValid(userToken) }
        coVerify { repository.isUserValid(userId) }
        coVerify { callback.invoke() }
    }

    @Ignore("This test is failing for something related with mocking the ApplicationCall object")
    @Test
    fun `test verify user invalid token`() {
        coEvery { repository.isTokenValid(null) } returns false
        coEvery { callback.invoke() } just Runs
        every { logger.error("Returned 401. $INVALID_USER_TOKEN_RESPONSE") } just Runs
        coEvery { call.respondText (text= INVALID_USER_TOKEN_RESPONSE, status = HttpStatusCode.Unauthorized) } just Runs


        runBlocking {
            userManager.verifyUser(call, logger, null, userId, callback)
        }

        coVerify { repository.isTokenValid(null) }
        verify { logger.error("Returned 401. $INVALID_USER_TOKEN_RESPONSE") }
        coVerify { call.respondText (text= INVALID_USER_TOKEN_RESPONSE, status = HttpStatusCode.Unauthorized) }
        coVerify(exactly = 0) { callback.invoke() }

    }

    @Ignore("This test is failing for something related with mocking the ApplicationCall object")
    @Test
    fun `test verify user invalid user`() {
        coEvery { repository.isTokenValid(userToken) } returns true
        coEvery { repository.isTokenValid(userId) } returns false
        coEvery { callback.invoke() } just Runs
        every { logger.error("Returned 401. $INVALID_USER_ID_RESPONSE") } just Runs
        coEvery { call.respondText (text= INVALID_USER_ID_RESPONSE, status = HttpStatusCode.Unauthorized) } just Runs


        runBlocking {
            userManager.verifyUser(call, logger, null, userId, callback)
        }

        coVerify { repository.isTokenValid(userToken) }
        coEvery { repository.isTokenValid(userId) }
        verify { logger.error("Returned 401. $INVALID_USER_ID_RESPONSE") }
        coVerify { call.respondText (text= INVALID_USER_ID_RESPONSE, status = HttpStatusCode.Unauthorized) }
        coVerify(exactly = 0) { callback.invoke() }

    }
}
