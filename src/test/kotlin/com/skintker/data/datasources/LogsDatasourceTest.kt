package com.skintker.data.datasources

import com.skintker.TestConstants.additionalData
import com.skintker.TestConstants.additionalDataEdited
import com.skintker.TestConstants.foodList
import com.skintker.TestConstants.foodList2
import com.skintker.TestConstants.idValues
import com.skintker.TestConstants.idValues2
import com.skintker.TestConstants.irritation
import com.skintker.TestConstants.irritation2
import com.skintker.TestConstants.irritationEdited
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
import com.skintker.data.dto.logs.AdditionalData
import com.skintker.data.dto.logs.Irritation
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
        TestDatabaseFactory.init(TestDatabaseFactory.DatabaseInitialization.Log)
        logsDataSource = LogsDatasourceImpl(irritationDataSource, additionalDataDataSource)
    }

    @Test
    fun `test add new log`() {
        mockInsert(irritation, additionalData)

        val result = runBlocking { logsDataSource.addNewLog(idValues, foodList, irritation, additionalData) }

        verifyMockInsert(irritation, additionalData)
        assertEquals(1, result)
    }

    @Test
    fun `test get log success`() {
        mockInsert(irritation, additionalData)

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.getLog(idValues)
        }

        verifyMockInsert(irritation, additionalData)
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
        mockInsert(irritation, additionalData)

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.deleteLog(idValues)
            logsDataSource.getLog(idValues)
        }

        verifyMockInsert(irritation, additionalData)
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
        mockInsert(irritation, additionalData,irritation2, additionalDataEdited)

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.addNewLog(idValues2, foodList2, irritation2, additionalDataEdited)
            logsDataSource.deleteAllLogs(userId)
            logsDataSource.getAllLogs(userId)
        }

        verifyMockInsert(irritation, additionalData)
        verifyMockInsert(irritation2, additionalDataEdited)
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
        mockInsert(irritation, additionalData)
        mockEdit(irritationEdited, additionalDataEdited)

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.editLog(idValues, foodList2, irritationEdited, additionalDataEdited)
            logsDataSource.getLog(idValues)
        }

        verifyMockInsert(irritation, additionalData)
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
        mockInsert(irritation, additionalData)
        mockInsert(irritation2, additionalDataEdited)

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.addNewLog(idValues, foodList, irritation2, additionalDataEdited)
            logsDataSource.getAllLogs(userId)
        }

        verifyMockInsert(irritation, additionalData)
        verifyMockInsert(irritation2, additionalDataEdited)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)

    }

    @Test
    fun `test get all logs value `() {
        mockInsert(irritation, additionalData)
        mockInsert(irritation2, additionalDataEdited)

        val result = runBlocking {
            logsDataSource.addNewLog(idValues, foodList, irritation, additionalData)
            logsDataSource.addNewLog(idValues2, foodList2, irritation2, additionalDataEdited)
            logsDataSource.getAllLogs(userId)
        }

        verifyMockInsert(irritation, additionalData)
        verifyMockInsert(irritation2, additionalDataEdited)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
    }


    private fun mockInsert(
        irritation: Irritation,
        additionalData: AdditionalData,
        irritation2: Irritation? = null,
        additionalData2: AdditionalData? = null
    ) {
        runBlocking {

            irritation2?.let {
                coEvery {
                    irritationDataSource.addNewIrritation(any())
                } returns createIrritationEntity(irritation) andThen createIrritationEntity(irritation2)
            } ?: run {
                coEvery {
                    irritationDataSource.addNewIrritation(any())
                } returns createIrritationEntity(irritation)
            }

            additionalData2?.let {
                coEvery {
                    additionalDataDataSource.addNewAdditionalData(any())
                } returns createAdditionalDataEntity(additionalData) andThen createAdditionalDataEntity(additionalData2)
            } ?: run {
                coEvery {
                    additionalDataDataSource.addNewAdditionalData(any())
                } returns createAdditionalDataEntity(additionalData)
            }


        }
    }

    private suspend fun createIrritationEntity(irritation: Irritation) = dbQuery {
        IrritationEntity.new {
            value = irritation.overallValue
            zoneValues = irritation.zoneValues.joinToString(",")
        }
    }

    private suspend fun createAdditionalDataEntity(additionalData: AdditionalData) = dbQuery {
        AdditionalDataEntity.new {
            stressLevel = additionalData.stressLevel
            weatherHumidity = additionalData.weather.humidity
            weatherTemperature = additionalData.weather.temperature
            traveled = additionalData.travel.traveled
            travelCity = additionalData.travel.city
            alcoholLevel = additionalData.alcohol.level.name
            beers = additionalData.alcohol.beers.joinToString(",")
            wines = additionalData.alcohol.wines.joinToString(",")
            distilledDrinks = additionalData.alcohol.distilledDrinks.joinToString(",")
        }
    }

    private fun mockEdit(irritation: Irritation, additionalData: AdditionalData) {
        runBlocking {
            coEvery { irritationDataSource.editIrritation(any(), irritation) } returns
                    createIrritationEntity(irritation)
            coEvery {
                additionalDataDataSource.editAdditionalData(any(), additionalData)
            } returns createAdditionalDataEntity(additionalData)
        }
    }


    private fun verifyMockInsert(irritation: Irritation, additionalData: AdditionalData) {
        coVerify { irritationDataSource.addNewIrritation(irritation) }
        coVerify { additionalDataDataSource.addNewAdditionalData(additionalData) }
    }

    private fun verifyMockEdit() {
        coVerify { irritationDataSource.editIrritation(any(), irritationEdited) }
        coVerify { additionalDataDataSource.editAdditionalData(any(), additionalDataEdited) }
    }

}
