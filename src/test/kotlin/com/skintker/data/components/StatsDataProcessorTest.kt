package com.skintker.data.components

import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.stats.StatsAlcohol
import com.skintker.data.dto.stats.StatsStress
import com.skintker.data.dto.stats.StatsTravel
import com.skintker.data.dto.stats.StatsWeather
import org.junit.Assert
import org.junit.Test

class StatsDataProcessorTest {

    private var processor = StatsDataProcessor()

    @Test
    fun `get possible causes item list`() {
        val itemMap = mapOf(
            "Item1" to 20,
            "Item2" to 30,
            "Item3" to 5,
            "Item4" to 5,
            "Item5" to 10,
            "Item6" to 40
        )

        val causesList = processor.getFromItemList(itemMap, 0.2f)

        val expectedList = listOf("Item2", "Item6")
        Assert.assertEquals(expectedList, causesList)
    }

    @Test
    fun `get possible stress causes`() {
        val map = mapOf(
            6 to 30,
            4 to 30,
            5 to 10,
            8 to 10,
            9 to 5,
            1 to 5,
            2 to 5,
            3 to 5,
            7 to 0,
            10 to 0
        )

        val causes = processor.getFromStress(map, 6, 0.6f)

        val expectedValue = StatsStress(false, 6)
        Assert.assertEquals(expectedValue, causes)
    }

    @Test
    fun `get possible  travel causes`() {
        val travelMap = mapOf(true to 10, false to 90)
        val cityMap =
            mapOf(
                "Sidney" to 5,
                "NewYork" to 5,
                "Madrid" to 40,
                "Barcelona" to 40,
                "London" to 10
            )

        val causes = processor.getFromTravel(travelMap, cityMap, 0.6f)

        val expectedValue = StatsTravel(false, "Madrid")
        Assert.assertEquals(expectedValue, causes)
    }

    @Test
    fun `get possible weather humidity causes`() {
        val humidityCityMap = mapOf(
            1 to 25,
            2 to 25,
            3 to 20,
            4 to 5,
            5 to 25
        )

        val causes = processor.getFromWeatherHumidity(humidityCityMap, 0.5f)

        val expectedValue = StatsWeather.StatsHumidity(true, 5)
        Assert.assertEquals(expectedValue, causes)
    }

    @Test
    fun `get possible weather temperature causes`() {
        val temperatureMap = mapOf(
            1 to 10,
            2 to 50,
            3 to 25,
            4 to 15,
            5 to 0
        )

        val causes = processor.getFromWeatherTemperature(temperatureMap, 0.5f)

        val expectedValue = StatsWeather.StatsTemperature(true, 2)
        Assert.assertEquals(expectedValue, causes)
    }

    @Test
    fun `get alcohol cause`() {
        val alcoholMap = mapOf(
            AlcoholLevel.None to 30,
            AlcoholLevel.Few to 50,
            AlcoholLevel.FewWine to 10,
            AlcoholLevel.Some to 10,
        )
        val beerTypeMap = mapOf(
            "Ale" to 30,
            "Wheat" to 60,
            "Stout" to 10,
        )

        val cause = processor.getFromAlcohol(beerTypeMap, alcoholMap, 0.7f)

        Assert.assertEquals( StatsAlcohol(true, "Wheat"), cause)
    }

}