package com.skintker.repository

import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.dto.DailyLog
import com.skintker.data.repository.ReportsRepository
import com.skintker.model.LogIdValues
import com.skintker.model.SaveReportStatus
import com.skintker.data.repository.impl.ReportsRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import kotlin.test.assertEquals

class ReportsRepositoryTest : KoinTest {

    private val daoFacadeMock = mockk<LogsDatasource>()

    private lateinit var sut: ReportsRepository

    companion object {
        private const val USER_ID = "USER_1"
        private const val DATE = "04-01-2023"
        private val idValues = LogIdValues(userId = USER_ID, dayDate = DATE)
        private val foodList = listOf("Food1", "Food2")
        private val log = DailyLog(DATE, null, null, foodList)
    }

    @Before
    fun setup() {
        sut = ReportsRepositoryImpl(daoFacadeMock)
    }

    @Test
    fun `test save report saved success status`() {
        coEvery { daoFacadeMock.getLog(idValues) } returns null
        coEvery { daoFacadeMock.addNewLog(idValues, foodList) } returns log

        val status = runBlocking {
            sut.saveReport(USER_ID, log)
        }

        coEvery { daoFacadeMock.getLog(idValues) }
        coVerify { daoFacadeMock.addNewLog(idValues, foodList) }
        assertEquals(SaveReportStatus.Saved, status)
    }

    @Test
    fun `test save report save failed status`() {
        coEvery { daoFacadeMock.getLog(idValues) } returns null
        coEvery { daoFacadeMock.addNewLog(idValues, foodList) } returns null

        val status = runBlocking {
            sut.saveReport(USER_ID, log)
        }

        coEvery { daoFacadeMock.getLog(idValues) }
        coVerify { daoFacadeMock.addNewLog(idValues, foodList) }
        assertEquals(SaveReportStatus.SavingFailed, status)
    }

    @Test
    fun `test save report edit success status`() {
        coEvery { daoFacadeMock.getLog(idValues) } returns log
        coEvery { daoFacadeMock.editLog(idValues, foodList) } returns true

        val status = runBlocking {
            sut.saveReport(USER_ID, log)
        }

        coEvery { daoFacadeMock.getLog(idValues) }
        coVerify { daoFacadeMock.editLog(idValues, foodList) }
        assertEquals(SaveReportStatus.Edited, status)
    }

    @Test
    fun `test save report edit failed status`() {
        coEvery { daoFacadeMock.getLog(idValues) } returns log
        coEvery { daoFacadeMock.editLog(idValues, foodList) } returns false

        val status = runBlocking {
            sut.saveReport(USER_ID, log)
        }

        coEvery { daoFacadeMock.getLog(idValues) }
        coVerify { daoFacadeMock.editLog(idValues, foodList) }
        assertEquals(SaveReportStatus.EditingFailed, status)
    }

    @Test
    fun `test save report bad input invalid user id status`() {

        val status = runBlocking {
            sut.saveReport("", log)
        }

        assertEquals(SaveReportStatus.BadInput, status)
    }
    @Test
    fun `test save report bad input no log data status`() {

        val status = runBlocking {
            sut.saveReport(USER_ID, DailyLog(date = DATE))
        }

        assertEquals(SaveReportStatus.BadInput, status)
    }

}