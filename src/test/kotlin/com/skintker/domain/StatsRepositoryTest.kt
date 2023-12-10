package com.skintker.domain

import com.skintker.TestConstants.logList
import com.skintker.TestConstants.stats
import com.skintker.TestConstants.userId
import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.datasources.StatsDatasource
import com.skintker.domain.repository.StatsRepository
import com.skintker.domain.repository.impl.StatsRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StatsRepositoryTest{

    private val logsDatasource = mockk<LogsDatasource>()

    private val statsDatasource = mockk<StatsDatasource>()

    private lateinit var repository: StatsRepository

    @Before
    fun setup() {
        repository = StatsRepositoryImpl(logsDatasource,statsDatasource)
    }

    @Test
    fun `test calculate user stats`() {
        coEvery { logsDatasource.getAllLogs(userId) } returns logList
        coEvery { statsDatasource.calculateStats(logList) } returns stats

        val result = runBlocking {
            repository.calculateUserStats(userId = userId, statsThreshold = 5)
        }

        coVerify { logsDatasource.getAllLogs(userId)  }
        coVerify { statsDatasource.calculateStats(logList) }
        assertEquals(stats,result)
    }

    @Test
    fun `test calculate user stats no logs empty result`() {
        coEvery { logsDatasource.getAllLogs(userId) } returns emptyList()

        val result = runBlocking {
            repository.calculateUserStats(userId = userId, statsThreshold = 5)
        }

        coVerify { logsDatasource.getAllLogs(userId)  }
        assertNull(result)
    }
}
