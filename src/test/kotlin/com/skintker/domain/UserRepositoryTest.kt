package com.skintker.domain

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.skintker.TestConstants.userId
import com.skintker.data.datasources.UserDatasource
import com.skintker.domain.repository.UserRepository
import com.skintker.domain.repository.impl.UserRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserRepositoryTest{

    private val daoFacadeMock = mockk<UserDatasource>()

    private val firebaseMock = mockk<FirebaseAuth>()


    private lateinit var repository: UserRepository


    @Before
    fun setup() {
        repository = UserRepositoryImpl(daoFacadeMock,firebaseMock)
    }

    @Test
    fun `test is user valid user exist in database`() {
        coEvery { daoFacadeMock.getUser(userId) } returns true

        val status = runBlocking {
            repository.isUserValid(userId = userId)
        }

        coVerify { daoFacadeMock.getUser(userId) }
        assertTrue(status)
    }

    @Test
    fun `test is user valid user exist in firebase`() {
        coEvery { daoFacadeMock.getUser(userId) } returns false
        coEvery { firebaseMock.getUser(userId) } returns null
        coEvery { daoFacadeMock.addUser(userId) } returns true

        val status = runBlocking {
            repository.isUserValid(userId = userId)
        }

        coVerify { daoFacadeMock.getUser(userId) }
        coVerify { firebaseMock.getUser(userId)  }
        coVerify { daoFacadeMock.addUser(userId) }
        assertTrue(status)
    }

    @Test
    fun `test is user valid user doesn't exist in firebase`() {
        coEvery { daoFacadeMock.getUser(userId) } returns false
        coEvery { firebaseMock.getUser(userId) } throws
                FirebaseAuthException(FirebaseException(ErrorCode.UNAUTHENTICATED,"a",Exception()))

        val status = runBlocking {
            repository.isUserValid(userId = userId)
        }

        coVerify { daoFacadeMock.getUser(userId) }
        coVerify { firebaseMock.getUser(userId)  }
        assertFalse(status)
    }
}