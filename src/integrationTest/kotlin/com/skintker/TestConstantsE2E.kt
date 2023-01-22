package com.skintker

import com.skintker.data.dto.logs.AdditionalData
import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.logs.Irritation
import com.skintker.data.dto.stats.StatsAlcohol
import com.skintker.data.dto.stats.StatsDto
import com.skintker.data.dto.stats.StatsStress
import com.skintker.data.dto.stats.StatsTravel
import com.skintker.data.dto.stats.StatsWeather

object TestConstantsE2E {

    const val serverUrl = "http://0.0.0.0:8080"
    const val reportPath = "report"
    const val reportsPath = "reports"
    const val statsPath = "stats"

    const val fbUserId = "TUMLO0AIkUSwAI9KDMfDc0v7T4P2"

    //Irritation values
    private const val irritationOverallValue = 8
    private val irritationZones = listOf("IrritationZone")
    private val irritation = Irritation(irritationOverallValue, irritationZones)
    private const val irritationOverallValue2 = 5
    private val irritationZones2 = listOf("IrritationZone", "IrritationZone2")
    private val irritationEdited = Irritation(irritationOverallValue2, irritationZones2)

    //AdditionalData values
    private const val adStress = 10
    private const val adWeatherHumidity = 1
    private const val adWeatherTemperature = 5
    private const val adTraveled = true
    private const val adCity = "Madrid"
    private val adAlcohol = AlcoholLevel.Some
    private val adBeerTypes = listOf("Ale")
    private val weather = AdditionalData.Weather(adWeatherHumidity, adWeatherTemperature)
    private val travel = AdditionalData.Travel(adTraveled, adCity)
    private val additionalData = AdditionalData(adStress, weather, travel, adAlcohol, adBeerTypes)
    private const val adStress2 = 3
    private const val adWeatherHumidity2 = 1
    private const val adWeatherTemperature2 = 2
    private const val adTraveled2 = false
    private const val adCity2 = ""
    private val adAlcohol2 = AlcoholLevel.FewWine
    private val adBeerTypes2 = listOf("Stout")
    private val weather2 = AdditionalData.Weather(adWeatherHumidity2, adWeatherTemperature2)
    private val travel2 = AdditionalData.Travel(adTraveled2, adCity2)
    private val additionalDataEdited = AdditionalData(
        adStress2,
        weather2,
        travel2,
        adAlcohol2,
        adBeerTypes2
    )

    //Log data
    const val date = "31-12-2022"
    private const val date2 = "01-01-2023"
    private val foodList = listOf("meat", "fish")
    private val foodList2 = listOf("strawberry", "fish", "banana")
    val log = DailyLog(date, foodList, irritation, additionalData)
    val log2 = DailyLog(date2, foodList2, irritationEdited, additionalDataEdited)

    //Stats
    val stats = StatsDto(
        relevantLogs = 1,
        enoughData = false,
        dietaryCauses = emptyList(),
        mostAffectedZones = listOf("IrritationZone"),
        alcohol = StatsAlcohol(true),
        stress = StatsStress(true,10),
        travel = StatsTravel(true,"Madrid"),
        weather = StatsWeather(StatsWeather.StatsTemperature(true, 5), StatsWeather.StatsHumidity(true,1))
    )

}
