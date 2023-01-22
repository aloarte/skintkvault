package com.skintker.data.datasources

import com.skintker.TestConstants.additionalData
import com.skintker.TestConstants.additionalDataEdited
import com.skintker.TestDatabaseFactory
import com.skintker.data.datasources.impl.AdditionalDataDatasourceImpl
import com.skintker.data.db.logs.entities.EntityParsers.additionalDataEntityToBo
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class AdditionalDataDatasourceTest {

    private lateinit var dataSource : AdditionalDataDatasource

    @Before
    fun setup() {
        dataSource = AdditionalDataDatasourceImpl()
        TestDatabaseFactory.init(TestDatabaseFactory.DatabaseInitialization.Log)
    }

    @Test
    fun `test add new additional data`() {
        val result = runBlocking { dataSource.addNewAdditionalData(additionalData) }
        assertEquals(additionalData, additionalDataEntityToBo(result))
    }

    @Test
    fun `test get additional data success`() {
        val result = runBlocking {
            dataSource.addNewAdditionalData(additionalData)
            dataSource.getAdditionalData(1)
        }

        assertNotNull(result)
        assertEquals(additionalData, result)
    }

    @Test
    fun `test get additional data not found`() {
        val result = runBlocking {
            dataSource.getAdditionalData(1)
        }

        assertNull(result)
    }

    @Test
    fun `test delete additional data success`() {
        val result = runBlocking {
            dataSource.addNewAdditionalData(additionalData)
            dataSource.deleteAdditionalData(listOf(1))
            dataSource.getAdditionalData(1)
        }

        assertNull(result)
    }

    @Test
    fun `test delete additional data not found`() {
        val result = runBlocking {
            dataSource.deleteAdditionalData(listOf(1))
            dataSource.getAdditionalData(1)
        }

        assertNull(result)
    }

    @Test
    fun `test edit additional data success`() {
        val result = runBlocking {
            val entityValue = dataSource.addNewAdditionalData(additionalData)
            dataSource.editAdditionalData(entityValue.id.value, additionalDataEdited)
            dataSource.getAdditionalData(1)
        }

        assertNotNull(result)
        assertEquals(additionalDataEdited.stressLevel, result.stressLevel)
    }

    @Test
    fun `test get all additional data with value empty list`() {
        val result = runBlocking {
            dataSource.getAllAdditionalDataValue(10)
        }

        assertTrue(result.isEmpty())
    }

    @Test
    fun `test get all additional data with value`() {
        val result = runBlocking {
            dataSource.addNewAdditionalData(additionalData)
            dataSource.addNewAdditionalData(additionalData)
            dataSource.getAllAdditionalDataValue(10)
        }

        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)

    }

}
