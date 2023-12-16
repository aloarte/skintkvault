package com.skintker.data.datasources

import com.skintker.TestConstants.logList
import com.skintker.data.ALCOHOLIC_DRINKS_AMOUNT_THRESHOLD
import com.skintker.data.FOOD_AMOUNT_THRESHOLD
import com.skintker.data.STRESS_THRESHOLD
import com.skintker.data.TRAVEL_AMOUNT_THRESHOLD
import com.skintker.data.ZONES_AMOUNT_THRESHOLD
import com.skintker.data.components.StatsDataInitializer
import com.skintker.data.datasources.impl.StatsDatasourceImpl
import com.skintker.data.components.StatsDataProcessor
import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.stats.StatsAlcohol
import com.skintker.data.dto.stats.StatsDto
import com.skintker.data.dto.stats.StatsTravel
import com.skintker.data.dto.stats.StatsWeather
import com.skintker.data.model.InvolvedDataType
import com.skintker.data.model.LogsStatsData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class StatDatasourceTest {

    private val processor = mockk<StatsDataProcessor>()

    private val dataInitializer = mockk<StatsDataInitializer>()

    private lateinit var dataSource: StatsDatasource

    companion object {
        private val statsData = LogsStatsData()
        private val relatedFoods = listOf("Meat", "Soy sauce")
        private val relatedZones = listOf("Wrist")
        private val relatedBeers = listOf("Ale", "Wheat")
        private val alcoholStats = StatsAlcohol(
            isPossible = true,
            type = AlcoholLevel.Beer,
            suspiciousBeers = relatedBeers
        )
        private val travelStats = StatsTravel(isPossible = true, city = "Madrid")
        private val temperatureStats = StatsWeather.StatsTemperature(true, 2)
        private val humidityStats = StatsWeather.StatsHumidity(false, -1)
        private val stats = StatsDto(
            dietaryCauses = relatedFoods,
            mostAffectedZones = relatedZones,
            alcohol = alcoholStats,
            stress = true,
            travel = travelStats,
            weather = StatsWeather(
                humidity = humidityStats,
                temperature = temperatureStats
            )
        )
    }

    @Before
    fun setup() {
        dataSource = StatsDatasourceImpl(processor, dataInitializer)
    }

    @Test
    fun `test calculate stats`() {
        every { dataInitializer.prepareData(logList) } returns statsData
        every {
            processor.getInvolvedData(
                InvolvedDataType.Foods,
                statsData,
                FOOD_AMOUNT_THRESHOLD
            )
        } returns relatedFoods
        every {
            processor.getInvolvedData(
                InvolvedDataType.Zones,
                statsData,
                ZONES_AMOUNT_THRESHOLD
            )
        } returns relatedZones
        every { processor.isAlcoholInvolved(statsData, ALCOHOLIC_DRINKS_AMOUNT_THRESHOLD) } returns alcoholStats
        every { processor.isStressInvolved(statsData, STRESS_THRESHOLD) } returns true
        every { processor.isTravelingInvolved(statsData, TRAVEL_AMOUNT_THRESHOLD) } returns travelStats
        every { processor.isWeatherTemperatureInvolved(statsData) } returns temperatureStats
        every { processor.isWeatherHumidityInvolved(statsData) } returns humidityStats

        val result = dataSource.calculateStats(logList)

        verify { dataInitializer.prepareData(logList) }
        verify {
            processor.getInvolvedData(
                InvolvedDataType.Foods,
                statsData,
                FOOD_AMOUNT_THRESHOLD
            )
        }
        verify {
            processor.getInvolvedData(
                InvolvedDataType.Zones,
                statsData,
                ZONES_AMOUNT_THRESHOLD
            )
        }
        verify { processor.isAlcoholInvolved(statsData, ALCOHOLIC_DRINKS_AMOUNT_THRESHOLD) }
        verify { processor.isStressInvolved(statsData, STRESS_THRESHOLD) }
        verify { processor.isTravelingInvolved(statsData, TRAVEL_AMOUNT_THRESHOLD) }
        verify { processor.isWeatherTemperatureInvolved(statsData) }
        verify { processor.isWeatherHumidityInvolved(statsData) }
        assertEquals(stats, result)
    }
}
