package com.skintker.domain

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import com.google.firebase.auth.UserRecord
import com.skintker.TestConstants.userEmail
import com.skintker.TestConstants.userId
import com.skintker.TestConstants.userToken
import com.skintker.data.datasources.UserDatasource
import com.skintker.domain.model.UserResult
import com.skintker.domain.repository.UserRepository
import com.skintker.domain.repository.impl.UserRepositoryImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserRepositoryTest {

    private val datasource = mockk<UserDatasource>()

    private val firebase = mockk<FirebaseAuth>()

    private val fbUser = mockk<UserRecord>()

    private val fbToken = mockk<FirebaseToken>()

    private lateinit var repository: UserRepository


    @Before
    fun setup() {
        repository = UserRepositoryImpl(datasource, firebase)
    }

    @Test
    fun `test get firebase user by mail, valid user`() {
        every { firebase.getUserByEmail(userEmail) } returns fbUser
        every { fbUser.uid } returns userId

        val user = runBlocking {
            repository.getFirebaseUser(email = userEmail)
        }

        verify { firebase.getUserByEmail(userEmail) }
        assertEquals(userId, user)
    }

    @Test
    fun `test get firebase user by mail, bad user`() {
        every { firebase.getUserByEmail(userEmail) } returns fbUser
        every { fbUser.uid } returns userId

        val user = runBlocking {
            repository.getFirebaseUser(email = userEmail)
        }

        verify { firebase.getUserByEmail(userEmail) }
        assertEquals(userId, user)
    }

    @Test
    fun `test user exist true`() {
        coEvery { datasource.userExists(userId) } returns true

        val userExist = runBlocking {
            repository.userExists(userId = userId)
        }

        coVerify { datasource.userExists(userId) }
        assertTrue(userExist)
    }

    @Test
    fun `test user exist false`() {
        coEvery { datasource.userExists(userId) } returns false

        val userExist = runBlocking {
            repository.userExists(userId = userId)
        }

        coVerify { datasource.userExists(userId) }
        assertFalse(userExist)
    }

    @Test
    fun `test user exist false, null userId`() {
        val userExist = runBlocking {
            repository.userExists(userId = null)
        }

        coVerify(exactly = 0) { datasource.userExists(userId) }
        assertFalse(userExist)
    }

    @Test
    fun `test remove user`() {
        coEvery { datasource.deleteUser(userId) } just Runs

        runBlocking {
            repository.removeUser(userId = userId)
        }

        coVerify { datasource.deleteUser(userId) }
    }

    @Test
    fun `test add user, user already inserted on ddbb`() {
        coEvery { datasource.userExists(userId) } returns true

        val status = runBlocking {
            repository.addUser(userId = userId)
        }

        coVerify { datasource.userExists(userId) }
        verify(exactly = 0) { firebase.getUser(userId) }
        coVerify(exactly = 0) { datasource.addUser(userId) }
        assertEquals(UserResult.UserExist, status)
    }

    @Test
    fun `test add user, exist in firebase, inserted on ddbb`() {
        coEvery { datasource.userExists(userId) } returns false
        every { firebase.getUser(userId) } returns fbUser
        every { fbUser.isDisabled } returns false
        coEvery { datasource.addUser(userId) } returns true

        val status = runBlocking {
            repository.addUser(userId = userId)
        }

        coVerify { datasource.userExists(userId) }
        verify { firebase.getUser(userId) }
        coVerify { datasource.addUser(userId) }
        assertEquals(UserResult.UserInsert(true),status)
    }

    @Test
    fun `test add user, exist in firebase, user disabled, not inserted`() {
        coEvery { datasource.userExists(userId) } returns false
        every { firebase.getUser(userId) } returns fbUser
        every { fbUser.isDisabled } returns true

        val status = runBlocking {
            repository.addUser(userId = userId)
        }

        coVerify { datasource.userExists(userId) }
        verify { firebase.getUser(userId) }
        coVerify(exactly = 0) { datasource.addUser(userId) }
        assertEquals(UserResult.FirebaseDisabled,status)
    }

    @Test
    fun `test add user, don't exist in firebase`() {
        coEvery { datasource.userExists(userId) } returns false
        every { firebase.getUser(userId) } throws FirebaseAuthException(
            FirebaseException(ErrorCode.UNAUTHENTICATED, "Exception", Exception())
        )

        val status = runBlocking {
            repository.addUser(userId = userId)
        }

        coVerify { datasource.userExists(userId) }
        verify { firebase.getUser(userId) }
        coVerify(exactly = 0) { datasource.addUser(userId) }
        assertEquals(UserResult.FirebaseError("Exception"),status)
    }

    @Test
    fun `test is token valid, token valid and mail verified`() {
        every { firebase.verifyIdToken(userToken, true) } returns fbToken
        every { fbToken.isEmailVerified } returns true

        val status = runBlocking {
            repository.isTokenValid(userToken = userToken)
        }

        verify { firebase.verifyIdToken(userToken, true) }
        assertTrue(status)
    }

    @Test
    fun `test is token valid, token valid and mail not verified`() {
        every { firebase.verifyIdToken(userToken, true) } returns fbToken
        every { fbToken.isEmailVerified } returns false

        val status = runBlocking {
            repository.isTokenValid(userToken = userToken)
        }

        verify { firebase.verifyIdToken(userToken, true) }
        assertFalse(status)
    }

    @Test
    fun `test is token valid, null token`() {
        val status = runBlocking {
            repository.isTokenValid(userToken = null)
        }

        verify(exactly = 0) { firebase.verifyIdToken(any(), true) }
        assertFalse(status)
    }

    @Test
    fun `test is token valid, token doesn't exist on firebase`() {
        every { firebase.verifyIdToken(userToken, true) } throws
                FirebaseAuthException(FirebaseException(ErrorCode.UNAUTHENTICATED, "a", Exception()))

        val status = runBlocking {
            repository.isTokenValid(userToken = userToken)
        }

        verify { firebase.verifyIdToken(userToken, true) }
        assertFalse(status)
    }

}
