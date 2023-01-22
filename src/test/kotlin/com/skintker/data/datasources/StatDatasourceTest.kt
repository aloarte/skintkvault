package com.skintker.data.datasources

import com.skintker.TestConstants.foodList
import com.skintker.TestConstants.foodMap
import com.skintker.TestConstants.irritationZones
import com.skintker.TestConstants.logList
import com.skintker.TestConstants.stats
import com.skintker.TestConstants.statsAlcohol
import com.skintker.TestConstants.statsHumidity
import com.skintker.TestConstants.statsStress
import com.skintker.TestConstants.statsTemperature
import com.skintker.TestConstants.statsTravel
import com.skintker.TestConstants.zonesMap
import com.skintker.data.datasources.impl.StatsDatasourceImpl
import com.skintker.data.components.StatsDataProcessor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class StatDatasourceTest {

    private val statsProcessor = mockk<StatsDataProcessor>()

    private lateinit var dataSource: StatsDatasource

    @Before
    fun setup() {
        dataSource = StatsDatasourceImpl(statsProcessor)
    }

    @Test
    fun `test calculate stats`() {
        every { statsProcessor.getFromItemList(foodMap, any()) } returns foodList
        every { statsProcessor.getFromItemList(zonesMap, any()) } returns irritationZones
        every { statsProcessor.getFromAlcohol(any(), any(), any()) } returns statsAlcohol
        every { statsProcessor.getFromStress(any(), any(), any()) } returns statsStress
        every { statsProcessor.getFromTravel(any(), any(), any()) } returns statsTravel
        every { statsProcessor.getFromWeatherTemperature(any(), any()) } returns statsTemperature
        every { statsProcessor.getFromWeatherHumidity(any(), any()) } returns statsHumidity

        val result = dataSource.calculateStats(logList)

        verify { statsProcessor.getFromItemList(foodMap, any()) }
        verify { statsProcessor.getFromItemList(zonesMap, any()) }
        verify { statsProcessor.getFromAlcohol(any(), any(), any()) }
        verify { statsProcessor.getFromStress(any(), any(), any()) }
        verify { statsProcessor.getFromTravel(any(), any(), any()) }
        verify { statsProcessor.getFromWeatherTemperature(any(), any()) }
        verify { statsProcessor.getFromWeatherHumidity(any(), any()) }
        assertEquals(stats, result)
    }

}
