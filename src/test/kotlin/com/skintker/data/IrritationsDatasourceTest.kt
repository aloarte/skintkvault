package com.skintker.data

import com.skintker.data.datasources.impl.IrritationsDatasourceImpl
import com.skintker.data.db.DatabaseFactory
import com.skintker.data.dto.Irritation
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class IrritationsDatasourceTest {

    companion object {
        private const val irritationOverallValue = 10
        private val irritationZones = listOf("IrritationZone")
        private val irritation = Irritation(10, listOf("IrritationZone"))
        private val irritationEdited = Irritation(5,listOf("IrritationZone2"))
    }

    private val dataSource = IrritationsDatasourceImpl()

    @Before
    fun setup(){
        DatabaseFactory.init()
    }

    @Test
    fun `test add new irritation`() {
        val result = runBlocking { dataSource.addNewIrritation(irritation) }

        assertEquals(irritationOverallValue, result.value)
        assertEquals(irritationZones.joinToString(","), result.zoneValues)
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
            dataSource.editIrritation(entityValue.id.value,irritationEdited)
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