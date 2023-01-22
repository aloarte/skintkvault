package com.skintker.data.datasources

import com.skintker.TestConstants
import com.skintker.TestConstants.adAlcohol2
import com.skintker.TestConstants.adStress
import com.skintker.TestConstants.adStress2
import com.skintker.TestConstants.additionalData
import com.skintker.TestConstants.additionalDataEdited
import com.skintker.TestConstants.foodList
import com.skintker.TestConstants.foodList2
import com.skintker.TestConstants.idValues
import com.skintker.TestConstants.irritation
import com.skintker.TestConstants.irritationEdited
import com.skintker.TestConstants.irritationOverallValue
import com.skintker.TestConstants.irritationOverallValue2
import com.skintker.TestConstants.irritationZones
import com.skintker.TestConstants.irritationZones2
import com.skintker.TestConstants.log
import com.skintker.TestConstants.logEdited
import com.skintker.TestConstants.userId
import com.skintker.TestDatabaseFactory
import com.skintker.data.datasources.impl.AdditionalDataDatasourceImpl
import com.skintker.data.datasources.impl.IrritationsDatasourceImpl
import com.skintker.data.datasources.impl.LogsDatasourceImpl
import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.db.logs.entities.AdditionalDataEntity
import com.skintker.data.db.logs.entities.IrritationEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LogsDatasourceTest {

    private lateinit var logsDataSource: LogsDatasource

    private val irritationDataSource = mockk<IrritationsDatasourceImpl>()

    private val additionalDataDataSource = mockk<AdditionalDataDatasourceImpl>()

    @Before
    fun setup() {
        logsDataSource = LogsDatasourceImpl(irritationDataSource, additionalDataDataSource)
        TestDatabaseFactory.init(TestDatabaseFactory.DatabaseInitialization.Log)
    }

    @Test
    fun `test add new log`() {
        mockInsert()

        val result = runBlocking { logsDataSource.addNewLog(idValues, foodList, irritation, additionalData) }

        verifyMockInsert()
        assertEquals(1, result)
    }

    @Test
    fun `test get log success`() {
        mockInsert()

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.getLog(idValues)
        }

        verifyMockInsert()
        assertNotNull(result)
        assertEquals(log, result)
    }

    @Test
    fun `test get log not found`() {
        val result = runBlocking {
            logsDataSource.getLog(idValues)
        }

        assertNull(result)
    }

    @Test
    fun `test delete log success`() {
        mockInsert()

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.deleteLog(idValues)
            logsDataSource.getLog(idValues)
        }

        verifyMockInsert()
        assertNull(result)
    }

    @Test
    fun `test delete log not found`() {
        val result = runBlocking {
            logsDataSource.deleteLog(idValues)
            logsDataSource.getLog(idValues)
        }

        assertNull(result)
    }

    @Test
    fun `test delete all logs `() {
        mockInsert()

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.deleteAllLogs(userId)
            logsDataSource.getAllLogs(userId)
        }

        verifyMockInsert()
        assertEquals(emptyList(), result)
    }

    @Test
    fun `test delete all logs not found `() {
        val result = runBlocking {
            logsDataSource.deleteAllLogs(userId)
            logsDataSource.getAllLogs(userId)
        }

        assertEquals(emptyList(), result)
    }

    @Test
    fun `test edit log success`() {
        mockInsert()
        mockEdit()

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.editLog(idValues, foodList2, irritationEdited, additionalDataEdited)
            logsDataSource.getLog(idValues)
        }

        verifyMockInsert()
        verifyMockEdit()
        assertNotNull(result)
        assertEquals(logEdited.date, result.date)
        assertEquals(logEdited.foodList, result.foodList)
        assertEquals(logEdited.additionalData.stressLevel, result.additionalData.stressLevel)
        assertEquals(logEdited.irritation, result.irritation)
    }

    @Test
    fun `test get all log from user empty list`() {
        val result = runBlocking {
            logsDataSource.getAllLogs(userId)
        }

        assertTrue(result.isEmpty())
    }

    @Test
    fun `test get all log from user with value`() {
        mockInsert()

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.getAllLogs(userId)
        }

        verifyMockInsert()
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)

    }

    @Test
    fun `test get all logs value `() {
        mockInsert()

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.getAllLogs(userId)
        }

        verifyMockInsert()
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
    }


    private fun mockInsert() {
        runBlocking {
            coEvery {
                irritationDataSource.addNewIrritation(irritation)
            } returns createIrritationEntity(true)
            coEvery {
                additionalDataDataSource.addNewAdditionalData(additionalData)
            } returns createAdditionalDataEntity(true)
        }
    }

    private suspend fun createIrritationEntity(added: Boolean) = dbQuery {
        IrritationEntity.new {
            value = if (added) irritationOverallValue else irritationOverallValue2
            zoneValues = if (added) {
                irritationZones.joinToString(",")
            } else {
                irritationZones2.joinToString(",")
            }
        }
    }

    private suspend fun createAdditionalDataEntity(added: Boolean) = dbQuery {
        AdditionalDataEntity.new {
            stressLevel = if (added) adStress else adStress2
            weatherHumidity = if (added) TestConstants.weather.humidity else TestConstants.weather2.humidity
            weatherTemperature = if (added) TestConstants.weather.temperature else TestConstants.weather2.temperature
            traveled = if (added) TestConstants.travel.traveled else TestConstants.travel2.traveled
            travelCity = if (added) TestConstants.travel.city else TestConstants.travel2.city
            alcoholLevel = if (added) TestConstants.adAlcohol.name else adAlcohol2.name
            beerTypes = if (added) {
                TestConstants.adBeerTypes.joinToString(",")
            } else {
                TestConstants.adBeerTypes2.joinToString(",")
            }
        }
    }

    private fun mockEdit() {
        runBlocking {
            coEvery { irritationDataSource.editIrritation(any(), irritationEdited) } returns createIrritationEntity(
                false
            )
            coEvery {
                additionalDataDataSource.editAdditionalData(
                    any(),
                    additionalDataEdited
                )
            } returns createAdditionalDataEntity(false)
        }
    }


    private fun verifyMockInsert() {
        coVerify { irritationDataSource.addNewIrritation(irritation) }
        coVerify { additionalDataDataSource.addNewAdditionalData(additionalData) }
    }

    private fun verifyMockEdit() {
        coVerify { irritationDataSource.editIrritation(any(), irritationEdited) }
        coVerify { additionalDataDataSource.editAdditionalData(any(), additionalDataEdited) }
    }

}
