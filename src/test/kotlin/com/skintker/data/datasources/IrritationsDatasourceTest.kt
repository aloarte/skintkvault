package com.skintker.data.datasources

import com.skintker.TestConstants.irritation
import com.skintker.TestConstants.irritationEdited
import com.skintker.TestDatabaseFactory
import com.skintker.data.datasources.impl.IrritationsDatasourceImpl
import com.skintker.data.db.logs.entities.EntityParsers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IrritationsDatasourceTest {

    private lateinit var dataSource : IrritationsDatasource

    @Before
    fun setup(){
        dataSource = IrritationsDatasourceImpl()
        TestDatabaseFactory.init(TestDatabaseFactory.DatabaseInitialization.Log)
    }

    @Test
    fun `test add new irritation`() {
        val result = runBlocking { dataSource.addNewIrritation(irritation) }

        assertEquals(irritation,  EntityParsers.irritationEntityToBo(result))
    }

    @Test
    fun `test get irritation success`() {
        val result = runBlocking {
            dataSource.addNewIrritation(irritation)
            dataSource.getIrritation(1)
        }

        assertNotNull(result)
        assertEquals(irritation, result) }

    @Test
    fun `test get irritation not found`() {
        val result = runBlocking {
            dataSource.getIrritation(1)
        }

        assertNull(result)
    }

    @Test
    fun `test delete irritation success`() {
        val result=  runBlocking {
            dataSource.addNewIrritation(irritation)
            dataSource.deleteIrritations(listOf(1))
            dataSource.getIrritation(1)
        }

        assertNull(result)
    }

    @Test
    fun `test delete irritation not found`() {
        val result=  runBlocking {
            dataSource.deleteIrritations(listOf(1))
            dataSource.getIrritation(1)
        }

        assertNull(result)
    }

    @Test
    fun `test edit irritation success`() {
        val result=  runBlocking {
            val entityValue = dataSource.addNewIrritation(irritation)
            dataSource.editIrritation(entityValue.id.value, irritationEdited)
            dataSource.getIrritation(1)
        }

        assertNotNull(result)
        assertEquals(irritationEdited, result)
    }

    @Test
    fun `test get all irritations with value empty list`() {
        val result=  runBlocking {
            dataSource.getAllIrritationsWithValue(10)
        }

        assertTrue(result.isEmpty())
    }

    @Test
    fun `test get all irritations with value`() {
        val result=  runBlocking {
            dataSource.addNewIrritation(irritation)
            dataSource.addNewIrritation(irritation)
            dataSource.getAllIrritationsWithValue(10)
        }

        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)

    }

}
