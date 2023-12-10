package com.skintker.data.components

import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.stats.StatsAlcohol
import com.skintker.data.dto.stats.StatsTravel
import com.skintker.data.dto.stats.StatsWeather
import com.skintker.data.model.InvolvedDataType
import com.skintker.data.model.LogsStatsData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StatsDataProcessorTest {

    private val calculator = mockk<StatisticsCalculator>()

    private lateinit var processor: StatsDataProcessor

    companion object {

        private val irritationLevels = listOf(10, 2, 7, 4, 3, 8)
        private val stressLevels = listOf(1, 2, 3, 5, 5, 1)
        private val temperatureLevels = listOf(1, 2, 3, 5, 5, 1)
        private val humidityLevels = listOf(1, 2, 3, 5, 5, 1)
        private val traveledLevels = listOf(true, false, false, false, true)
        private val traveledCityMap = mapOf("city 1" to 10, "city 2" to 2)
        private val alcoholTypeMapNone = listOf(
            AlcoholLevel.None,
            AlcoholLevel.Beer,
            AlcoholLevel.None,
            AlcoholLevel.Beer,
            AlcoholLevel.None,
            AlcoholLevel.None
        )
        private val alcoholTypeMapBeer = listOf(
            AlcoholLevel.Beer,
            AlcoholLevel.None,
            AlcoholLevel.Beer,
            AlcoholLevel.None,
            AlcoholLevel.Beer,
            AlcoholLevel.Beer
        )
        private val alcoholTypeMapWine = listOf(
            AlcoholLevel.Wine,
            AlcoholLevel.None,
            AlcoholLevel.Wine,
            AlcoholLevel.None,
            AlcoholLevel.Wine,
            AlcoholLevel.Wine
        )
        private val alcoholTypeMapDrink = listOf(
            AlcoholLevel.Distilled,
            AlcoholLevel.None,
            AlcoholLevel.Distilled,
            AlcoholLevel.None,
            AlcoholLevel.Distilled,
            AlcoholLevel.Distilled
        )
        private val alcoholTypeMapMixed = listOf(
            AlcoholLevel.Mixed,
            AlcoholLevel.None,
            AlcoholLevel.Mixed,
            AlcoholLevel.None,
            AlcoholLevel.Mixed,
            AlcoholLevel.Mixed
        )
        private val beersMap = mapOf("beer 1" to 4, "beer 2" to 2)
        private val winesMap = mapOf("wine 1" to 4, "wine 2" to 2)
        private val drinksMap = mapOf("drink 1" to 4, "drink 2" to 2)

        private val foodsLevels = mapOf(
            "food 1" to listOf(true, false, false, false, true),
            "food 2" to listOf(false, false, false, true, false)
        )
        private val zonesLevels = mapOf(
            "zone 1" to listOf(true, true, true, true, true),
            "zone 2" to listOf(false, false, false, false, false)
        )
        private val beersLevels = mapOf(
            "beer 1" to listOf(true, false, true, false, true),
            "beer 2" to listOf(false, true, false, true, false)
        )
        private val winesLevels = mapOf(
            "wine 1" to listOf(true, false, true, false, true),
            "wine 2" to listOf(false, true, false, true, false)
        )
        private val drinksLevels = mapOf(
            "drink 1" to listOf(true, false, true, false, true),
            "drink 2" to listOf(false, true, false, true, false)
        )

        val stats = LogsStatsData(
            irritationLevels = irritationLevels,
            stressLevels = stressLevels,
            temperatureLevels = temperatureLevels,
            humidityLevels = humidityLevels,
            foodsLevels = foodsLevels,
            zonesLevels = zonesLevels,
            alcoholTypeMap = alcoholTypeMapNone,
            beersLevels = beersLevels,
            winesLevels = winesLevels,
            drinksLevels = drinksLevels,
            traveledLevels = traveledLevels,
            traveledCityMap = traveledCityMap
        )
    }

    @Before
    fun setup() {
        processor = StatsDataProcessor(calculator)
    }

    @Test
    fun `test get involved data foods significant values`() {
        every { calculator.getCorrelations(stats.irritationLevels, stats.foodsLevels) } returns
                mapOf("food 1" to 0.4, "food 2" to 0.6)

        val involvedFoods = processor.getInvolvedData(InvolvedDataType.Foods, stats, 0.5)

        verify { calculator.getCorrelations(stats.irritationLevels, stats.foodsLevels) }
        Assert.assertEquals(listOf("food 2"), involvedFoods)
    }

    @Test
    fun `test get involved data zones NaN values`() {
        every { calculator.getCorrelations(stats.irritationLevels, stats.zonesLevels) } returns
                mapOf("zone 1" to Double.NaN, "zone 2" to Double.NaN)

        val involvedZones = processor.getInvolvedData(InvolvedDataType.Zones, stats, 0.5)

        verify { calculator.getCorrelations(stats.irritationLevels, stats.zonesLevels) }
        Assert.assertEquals(emptyList<String>(), involvedZones)
    }

    @Test
    fun `test get involved data beers below threshold values`() {
        every { calculator.getCorrelations(stats.irritationLevels, stats.beersLevels) } returns
                mapOf("beer 1" to 0.4, "beer 2" to 0.2)

        val involvedBeers = processor.getInvolvedData(InvolvedDataType.Beers, stats, 0.5)

        verify { calculator.getCorrelations(stats.irritationLevels, stats.beersLevels) }
        Assert.assertEquals(emptyList<String>(), involvedBeers)
    }

    @Test
    fun `test get involved data wines below threshold values`() {
        every { calculator.getCorrelations(stats.irritationLevels, stats.winesLevels) } returns
                mapOf("wine 1" to 0.4, "wine 2" to 0.2)

        val involvedWines = processor.getInvolvedData(InvolvedDataType.Wines, stats, 0.5)

        verify { calculator.getCorrelations(stats.irritationLevels, stats.winesLevels) }
        Assert.assertEquals(emptyList<String>(), involvedWines)
    }

    @Test
    fun `test get involved data drinks below threshold values`() {
        every { calculator.getCorrelations(stats.irritationLevels, stats.drinksLevels) } returns
                mapOf("drink 1" to 0.4, "drink 2" to 0.2)

        val involvedDrinks = processor.getInvolvedData(InvolvedDataType.Drinks, stats, 0.5)

        verify { calculator.getCorrelations(stats.irritationLevels, stats.drinksLevels) }
        Assert.assertEquals(emptyList<String>(), involvedDrinks)
    }

    @Test
    fun `test is stress involved NaN correlation`() {
        every { calculator.calculateCorrelation(stats.irritationLevels, stats.stressLevels) } returns Double.NaN

        Assert.assertFalse(processor.isStressInvolved(stats, 0.5))

        verify { calculator.calculateCorrelation(stats.irritationLevels, stats.stressLevels) }
    }

    @Test
    fun `test is stress involved correlation below threshold`() {
        every { calculator.calculateCorrelation(stats.irritationLevels, stats.stressLevels) } returns 0.3

        Assert.assertFalse(processor.isStressInvolved(stats, 0.5))

        verify { calculator.calculateCorrelation(stats.irritationLevels, stats.stressLevels) }
    }

    @Test
    fun `test is stress involved correlation`() {
        every { calculator.calculateCorrelation(stats.irritationLevels, stats.stressLevels) } returns 0.6

        Assert.assertTrue(processor.isStressInvolved(stats, 0.5))

        verify { calculator.calculateCorrelation(stats.irritationLevels, stats.stressLevels) }
    }

    @Test
    fun `test is traveling involved true`() {
        every { calculator.calculateCorrelationBinary(stats.irritationLevels, stats.traveledLevels) } returns 0.6

        val statsTravel = processor.isTravelingInvolved(stats, 0.5)

        verify { calculator.calculateCorrelationBinary(stats.irritationLevels, stats.traveledLevels) }
        Assert.assertEquals(StatsTravel(true, "city 1"), statsTravel)
    }

    @Test
    fun `test is traveling involved false`() {
        every { calculator.calculateCorrelationBinary(stats.irritationLevels, stats.traveledLevels) } returns 0.4

        val statsTravel = processor.isTravelingInvolved(stats, 0.5)

        verify { calculator.calculateCorrelationBinary(stats.irritationLevels, stats.traveledLevels) }
        Assert.assertEquals(StatsTravel(false, "city 1"), statsTravel)
    }

    @Test
    fun `test is traveling involved false NaN`() {
        every { calculator.calculateCorrelationBinary(stats.irritationLevels, stats.traveledLevels) } returns Double.NaN

        val statsTravel = processor.isTravelingInvolved(stats, 0.5)

        verify { calculator.calculateCorrelationBinary(stats.irritationLevels, stats.traveledLevels) }
        Assert.assertEquals(StatsTravel(false, "city 1"), statsTravel)
    }

    @Test
    fun `test is weather temperature involved true`() {
        every { calculator.isAnyGroupSignificant(any()) } returns true

        val statsWeather = processor.isWeatherTemperatureInvolved(stats)

        verify { calculator.isAnyGroupSignificant(any()) }
        Assert.assertEquals(StatsWeather.StatsTemperature(true, 1), statsWeather)
    }

    @Test
    fun `test is weather temperature involved false`() {
        every { calculator.isAnyGroupSignificant(any()) } returns false

        val statsWeather = processor.isWeatherTemperatureInvolved(stats)

        verify { calculator.isAnyGroupSignificant(any()) }
        Assert.assertEquals(StatsWeather.StatsTemperature(false, -1), statsWeather)
    }

    @Test
    fun `test is weather temperature involved false insufficient data 1 relevant group`() {
        val only1SignificantGroupData = listOf(1, 1, 1, 5, 1, 1)

        val statsWeather =
            processor.isWeatherTemperatureInvolved(stats.copy(temperatureLevels = only1SignificantGroupData))

        verify(exactly = 0) { calculator.isAnyGroupSignificant(any()) }
        Assert.assertEquals(StatsWeather.StatsTemperature(false, -1), statsWeather)
    }

    @Test
    fun `test is weather temperature involved false insufficient data only 1 group`() {
        val only1GroupData = listOf(1, 1, 1, 1, 1, 1)

        val statsWeather = processor.isWeatherTemperatureInvolved(stats.copy(temperatureLevels = only1GroupData))

        verify(exactly = 0) { calculator.isAnyGroupSignificant(any()) }
        Assert.assertEquals(StatsWeather.StatsTemperature(false, -1), statsWeather)
    }

    @Test
    fun `test is humidity temperature involved true`() {
        every { calculator.isAnyGroupSignificant(any()) } returns true

        val statsWeather = processor.isWeatherHumidityInvolved(stats)

        verify { calculator.isAnyGroupSignificant(any()) }
        Assert.assertEquals(StatsWeather.StatsHumidity(true, 1), statsWeather)
    }

    @Test
    fun `test is alcohol involved true beers`() {
        every { calculator.isAnyGroupSignificant(any()) } returns true
        every { calculator.getCorrelations(irritationLevels, beersLevels) } returns
                mapOf("beer 1" to 0.6, "beer 2" to Double.NaN)

        val alcoholStats = processor.isAlcoholInvolved(stats.copy(alcoholTypeMap = alcoholTypeMapBeer), 0.5)

        verify { calculator.isAnyGroupSignificant(any()) }
        verify { calculator.getCorrelations(irritationLevels, beersLevels) }
        verify(exactly = 0) { calculator.getCorrelations(irritationLevels, winesLevels) }
        verify(exactly = 0) { calculator.getCorrelations(irritationLevels, drinksLevels) }
        Assert.assertEquals(
            StatsAlcohol(isPossible = true, type = AlcoholLevel.Beer, suspiciousBeers = listOf("beer 1")),
            alcoholStats
        )
    }

    @Test
    fun `test is alcohol involved true wine`() {
        every { calculator.isAnyGroupSignificant(any()) } returns true
        every { calculator.getCorrelations(irritationLevels, winesLevels) } returns
                mapOf("wine 1" to 0.6, "wine 2" to Double.NaN)

        val alcoholStats = processor.isAlcoholInvolved(stats.copy(alcoholTypeMap = alcoholTypeMapWine), 0.5)

        verify { calculator.isAnyGroupSignificant(any()) }
        verify(exactly = 0) { calculator.getCorrelations(irritationLevels, beersLevels) }
        verify { calculator.getCorrelations(irritationLevels, winesLevels) }
        verify(exactly = 0) { calculator.getCorrelations(irritationLevels, drinksLevels) }
        Assert.assertEquals(
            StatsAlcohol(isPossible = true, type = AlcoholLevel.Wine, suspiciousWines = listOf("wine 1")),
            alcoholStats
        )
    }

    @Test
    fun `test is alcohol involved true distilled`() {
        every { calculator.isAnyGroupSignificant(any()) } returns true
        every { calculator.getCorrelations(irritationLevels, drinksLevels) } returns
                mapOf("drink 1" to 0.6, "drink 2" to Double.NaN)

        val alcoholStats = processor.isAlcoholInvolved(stats.copy(alcoholTypeMap = alcoholTypeMapDrink), 0.5)

        verify { calculator.isAnyGroupSignificant(any()) }
        verify(exactly = 0) { calculator.getCorrelations(irritationLevels, beersLevels) }
        verify(exactly = 0) { calculator.getCorrelations(irritationLevels, winesLevels) }
        verify { calculator.getCorrelations(irritationLevels, drinksLevels) }
        Assert.assertEquals(
            StatsAlcohol(isPossible = true, type = AlcoholLevel.Distilled, suspiciousDrinks = listOf("drink 1")),
            alcoholStats
        )
    }

    @Test
    fun `test is alcohol involved true mixed`() {
        every { calculator.isAnyGroupSignificant(any()) } returns true
        every { calculator.getCorrelations(irritationLevels, beersLevels) } returns
                mapOf("beer 1" to 0.7, "beer 2" to Double.NaN)
        every { calculator.getCorrelations(irritationLevels, winesLevels) } returns
                mapOf("wine 1" to 0.55, "wine 2" to Double.NaN)
        every { calculator.getCorrelations(irritationLevels, drinksLevels) } returns
                mapOf("drink 1" to 0.6, "drink 2" to Double.NaN)

        val alcoholStats = processor.isAlcoholInvolved(stats.copy(alcoholTypeMap = alcoholTypeMapMixed), 0.5)

        verify { calculator.isAnyGroupSignificant(any()) }
        verify { calculator.getCorrelations(irritationLevels, beersLevels) }
        verify { calculator.getCorrelations(irritationLevels, winesLevels) }
        verify { calculator.getCorrelations(irritationLevels, drinksLevels) }
        Assert.assertEquals(
            StatsAlcohol(
                isPossible = true,
                type = AlcoholLevel.Mixed,
                suspiciousBeers = listOf("beer 1"),
                suspiciousWines = listOf("wine 1"),
                suspiciousDrinks = listOf("drink 1")
            ),
            alcoholStats
        )
    }

    @Test
    fun `test is alcohol involved false`() {
        every { calculator.isAnyGroupSignificant(any()) } returns false

        val alcoholStats = processor.isAlcoholInvolved(stats, 0.5)

        verify { calculator.isAnyGroupSignificant(any()) }
        verify(exactly = 0) { calculator.getCorrelations(irritationLevels, beersLevels) }
        verify(exactly = 0) { calculator.getCorrelations(irritationLevels, winesLevels) }
        verify(exactly = 0) { calculator.getCorrelations(irritationLevels, drinksLevels) }
        Assert.assertEquals(
            StatsAlcohol(isPossible = false, type = AlcoholLevel.None),
            alcoholStats
        )
    }

}
