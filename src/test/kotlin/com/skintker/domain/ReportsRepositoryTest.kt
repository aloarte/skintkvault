package com.skintker.domain

import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.dto.logs.AdditionalData
import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.logs.Irritation
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.model.LogIdValues
import com.skintker.domain.model.SaveReportStatus
import com.skintker.domain.repository.impl.ReportsRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReportsRepositoryTest{

    private val daoFacadeMock = mockk<LogsDatasource>()

    private lateinit var repository: ReportsRepository

    companion object {
        private const val USER_ID = "USER_1"
        private const val DATE = "04-01-2023"
        private val idValues = LogIdValues(userId = USER_ID, dayDate = DATE)
        private val foodList = listOf("Food1", "Food2")
        private const val overallValue = 10
        private val zoneList = listOf("zone1", "zone2")
        private const val stressLevel = 10
        private const val humidity = 4
        private const val temperature = 4
        private const val traveled = true
        private const val city = "Madrid"
        private val alcoholLevel =AdditionalData.Alcohol(
            AlcoholLevel.None,
            emptyList()
        )
        private val irritation = Irritation(overallValue, zoneList)
        private val additionalData = AdditionalData(
            stressLevel,
            AdditionalData.Weather(humidity, temperature),
            AdditionalData.Travel(traveled, city),
            alcoholLevel,
        )

        private val log = DailyLog(DATE,foodList, irritation, additionalData)
    }

    @Before
    fun setup() {
        repository = ReportsRepositoryImpl(daoFacadeMock)
    }

    @Test
    fun `test delete report success`() {
        coEvery { daoFacadeMock.deleteLog(idValues) } returns true

        val status = runBlocking {
            repository.deleteReport(idValues)
        }

        coVerify { daoFacadeMock.deleteLog(idValues) }
        assertTrue(status)
    }

    @Test
    fun `test delete report failed`() {
        coEvery { daoFacadeMock.deleteLog(idValues) } returns false

        val status = runBlocking {
            repository.deleteReport(idValues)
        }

        coVerify { daoFacadeMock.deleteLog(idValues) }
        assertFalse(status)
    }

    @Test
    fun `test delete reports success`() {
        coEvery { daoFacadeMock.deleteAllLogs(USER_ID) } returns true

        val status = runBlocking {
            repository.deleteReports(USER_ID)
        }

        coVerify { daoFacadeMock.deleteAllLogs(USER_ID) }
        assertTrue(status)
    }

    @Test
    fun `test delete reports failed`() {
        coEvery { daoFacadeMock.deleteAllLogs(USER_ID) } returns false

        val status = runBlocking {
            repository.deleteReports(USER_ID)
        }

        coVerify { daoFacadeMock.deleteAllLogs(USER_ID) }
        assertFalse(status)
    }


    @Test
    fun `test save report saved success status`() {
        coEvery { daoFacadeMock.getLog(idValues) } returns null
        coEvery { daoFacadeMock.addNewLog(idValues, foodList, irritation, additionalData) } returns 1

        val status = runBlocking {
            repository.saveReport(USER_ID, log)
        }

        coEvery { daoFacadeMock.getLog(idValues) }
        coVerify { daoFacadeMock.addNewLog(idValues, foodList, irritation, additionalData) }
        assertEquals(SaveReportStatus.Saved, status)
    }

    @Test
    fun `test save report save failed status`() {
        coEvery { daoFacadeMock.getLog(idValues) } returns null
        coEvery { daoFacadeMock.addNewLog(idValues, foodList, irritation, additionalData) } returns -1

        val status = runBlocking {
            repository.saveReport(USER_ID, log)
        }

        coEvery { daoFacadeMock.getLog(idValues) }
        coVerify { daoFacadeMock.addNewLog(idValues, foodList, irritation, additionalData) }
        assertEquals(SaveReportStatus.SavingFailed, status)
    }

    @Test
    fun `test save report edit success status`() {
        coEvery { daoFacadeMock.getLog(idValues) } returns log
        coEvery { daoFacadeMock.editLog(idValues, foodList, irritation, additionalData) } returns true

        val status = runBlocking {
            repository.saveReport(USER_ID, log)
        }

        coEvery { daoFacadeMock.getLog(idValues) }
        coVerify { daoFacadeMock.editLog(idValues, foodList, irritation, additionalData) }
        assertEquals(SaveReportStatus.Edited, status)
    }

    @Test
    fun `test save report edit failed status`() {
        coEvery { daoFacadeMock.getLog(idValues) } returns log
        coEvery { daoFacadeMock.editLog(idValues, foodList, irritation, additionalData) } returns false

        val status = runBlocking {
            repository.saveReport(USER_ID, log)
        }

        coEvery { daoFacadeMock.getLog(idValues) }
        coVerify { daoFacadeMock.editLog(idValues, foodList, irritation, additionalData) }
        assertEquals(SaveReportStatus.EditingFailed, status)
    }
}
