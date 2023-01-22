package com.skintker.data.datasources


import com.skintker.TestConstants.userId
import com.skintker.TestDatabaseFactory
import com.skintker.data.datasources.impl.UserDatasourceImpl
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class UserDatasourceTest {

    private lateinit var dataSource : UserDatasource

    @Before
    fun setup(){
        dataSource = UserDatasourceImpl()
        TestDatabaseFactory.init(TestDatabaseFactory.DatabaseInitialization.User)
    }

    @Test
    fun `test add new user`() {
        val result = runBlocking { dataSource.addUser(userId) }

        assertTrue(result)
    }

    @Test
    fun `test get user success`() {
        val result = runBlocking {
            dataSource.addUser(userId)
            dataSource.getUser(userId)
        }

        assertTrue(result)
    }

    @Test
    fun `test get user not found`() {
        val result = runBlocking {
            dataSource.getUser(userId)
        }

        assertFalse(result)
    }

    @Test
    fun `test delete user success`() {
        val result=  runBlocking {
            dataSource.getUser(userId)
            dataSource.deleteUser(userId)
            dataSource.getUser(userId)
        }

        assertFalse(result)
    }

    @Test
    fun `test delete user not found`() {
        val result=  runBlocking {
            dataSource.deleteUser(userId)
            dataSource.getUser(userId)
        }

        assertFalse(result)
    }

}
