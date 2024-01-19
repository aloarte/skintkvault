package com.skintker.domain

import com.skintker.TestConstants.userEmail
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
import junit.framework.TestCase.assertFalse
import kotlin.test.assertTrue

class UserManagerTest {

    private val repository = mockk<UserRepository>()

    private val call = mockk<ApplicationCall>()

    private val logger = mockk<Logger>()

    private val callbackVerify = mockk<suspend () -> Unit>()

    private val callbackMail = mockk<suspend (String) -> Unit>()

    private lateinit var userManager: UserManager

    @Before
    fun setup() {
        userManager = UserManager(repository)
    }

    @Test
    fun `test remove user`() {
        coEvery { repository.removeUser(userId) } just Runs

        runBlocking {
            userManager.removeUser(userId)
        }

        coVerify { repository.removeUser(userId) }
    }

    @Test
    fun `test add user, token valid, user inserted`() {
        coEvery { repository.userExists(userId) } returns false
        coEvery { repository.isTokenValid(userToken) } returns true
        coEvery { repository.addUser(userId) } returns true

        val inserted = runBlocking {
            userManager.addUser(userToken,userId)
        }

        coVerify { repository.userExists(userId) }
        coVerify { repository.isTokenValid(userToken) }
        coVerify { repository.addUser(userId) }
        assertTrue(inserted)
    }

    @Test
    fun `test add user, token valid, user already exist`() {
        coEvery { repository.userExists(userId) } returns true
        coEvery { repository.isTokenValid(userToken) } returns true

        val inserted = runBlocking {
            userManager.addUser(userToken,userId)
        }

        coVerify { repository.userExists(userId) }
        coVerify(exactly = 0) { repository.isTokenValid(userToken) }
        coVerify(exactly = 0) { repository.addUser(userToken) }
        assertFalse(inserted)
    }

    @Test
    fun `test add user, token valid, user not inserted`() {
        coEvery { repository.userExists(userId) } returns false
        coEvery { repository.isTokenValid(userToken) } returns true
        coEvery { repository.addUser(userId) } returns false

        val inserted = runBlocking {
            userManager.addUser(userToken,userId)
        }

        coVerify { repository.userExists(userId) }
        coVerify { repository.isTokenValid(userToken) }
        coVerify { repository.addUser(userId) }
        assertFalse(inserted)
    }

    @Test
    fun `test add user, token not valid`() {
        coEvery { repository.userExists(userId) } returns false
        coEvery { repository.isTokenValid(userToken) } returns false

        val inserted = runBlocking {
            userManager.addUser(userToken,userId)
        }

        coVerify { repository.userExists(userId) }
        coVerify { repository.isTokenValid(userToken) }
        coVerify(exactly = 0) { repository.addUser(any()) }
        assertFalse(inserted)
    }

    @Test
    fun `test verify user valid user and token`() {
        coEvery { repository.isTokenValid(userToken) } returns true
        coEvery { repository.userExists(userId) } returns true
        coEvery { callbackVerify.invoke() } just Runs

        runBlocking {
            userManager.verifyUser(call, logger, userToken, userId, callbackVerify)
        }

        coVerify { repository.isTokenValid(userToken) }
        coVerify { repository.userExists(userId) }
        coVerify { callbackVerify.invoke() }
    }

    @Ignore("This test is failing for something related with mocking the ApplicationCall object")
    @Test
    fun `test verify user invalid token`() {
        coEvery { repository.isTokenValid(null) } returns false
        coEvery { callbackVerify.invoke() } just Runs
        every { logger.error("Returned 401. $INVALID_USER_TOKEN_RESPONSE") } just Runs
        coEvery {
            call.respondText(
                text = INVALID_USER_TOKEN_RESPONSE,
                status = HttpStatusCode.Unauthorized
            )
        } just Runs

        runBlocking {
            userManager.verifyUser(call, logger, null, userId, callbackVerify)
        }

        coVerify { repository.isTokenValid(null) }
        verify { logger.error("Returned 401. $INVALID_USER_TOKEN_RESPONSE") }
        coVerify { call.respondText(text = INVALID_USER_TOKEN_RESPONSE, status = HttpStatusCode.Unauthorized) }
        coVerify(exactly = 0) { callbackVerify.invoke() }

    }

    @Ignore("This test is failing for something related with mocking the ApplicationCall object")
    @Test
    fun `test verify user invalid user`() {
        coEvery { repository.isTokenValid(userToken) } returns true
        coEvery { repository.isTokenValid(userId) } returns false
        coEvery { callbackVerify.invoke() } just Runs
        every { logger.error("Returned 401. $INVALID_USER_ID_RESPONSE") } just Runs
        coEvery {
            call.respondText(
                text = INVALID_USER_ID_RESPONSE,
                status = HttpStatusCode.Unauthorized
            )
        } just Runs


        runBlocking {
            userManager.verifyUser(call, logger, null, userId, callbackVerify)
        }

        coVerify { repository.isTokenValid(userToken) }
        coEvery { repository.isTokenValid(userId) }
        verify { logger.error("Returned 401. $INVALID_USER_ID_RESPONSE") }
        coVerify { call.respondText(text = INVALID_USER_ID_RESPONSE, status = HttpStatusCode.Unauthorized) }
        coVerify(exactly = 0) { callbackVerify.invoke() }

    }

    @Test
    fun `test get firebase user by mail, found user`() {
        coEvery { repository.getFirebaseUser(userEmail) } returns userId
        coEvery { repository.userExists(userId) } returns true
        coEvery { callbackMail.invoke(userId) } just Runs

        runBlocking {
            userManager.getFirebaseUserByMail(call, logger, userEmail, callbackMail)
        }

        coVerify { repository.getFirebaseUser(userEmail) }
        coVerify { repository.userExists(userId) }
        coVerify { callbackMail.invoke(userId) }
    }
}
