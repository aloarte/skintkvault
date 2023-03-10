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
import com.skintker.domain.model.LogIdValues
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object TestConstants {
    const val jsonBodyLog =
        "{\n" +
                "  \"date\": \"31-12-2022\",\n" +
                "  \"irritation\": {\n" +
                "    \"overallValue\": 8,\n" +
                "    \"zoneValues\": [\n" +
                "      \"IrritationZone\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"additionalData\": {\n" +
                "    \"stressLevel\": 10,\n" +
                "    \"weather\": {\n" +
                "      \"humidity\": 0,\n" +
                "      \"temperature\": 5\n" +
                "    },\n" +
                "    \"travel\": {\n" +
                "      \"traveled\": true,\n" +
                "      \"city\": \"Madrid\"\n" +
                "    },\n" +
                "    \"alcoholLevel\": \"Some\",\n" +
                "    \"beerTypes\": [\n" +
                "      \"Ale\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"foodList\": [\n" +
                "    \"meat\",\n" +
                "    \"fish\"\n" +
                "  ]\n" +
                "}"

    const val jsonStats = "{\n" +
            "  \"enoughData\": false,\n" +
            "  \"relevantLogs\": 2,\n" +
            "  \"dietaryCauses\": [\n" +
            "    \"meat\",\n" +
            "    \"fish\"\n" +
            "  ],\n" +
            "  \"mostAffectedZones\": [\n" +
            "    \"IrritationZone\"\n" +
            "  ],\n" +
            "  \"alcohol\": {\n" +
            "    \"isPossible\": true,\n" +
            "    \"beerType\": \"Ale\"\n" +
            "  },\n" +
            "  \"stress\": {\n" +
            "    \"isPossible\": false,\n" +
            "    \"level\": 10\n" +
            "  },\n" +
            "  \"travel\": {\n" +
            "    \"isPossible\": true,\n" +
            "    \"city\": \"Madrid\"\n" +
            "  },\n" +
            "  \"weather\": {\n" +
            "    \"temperature\": {\n" +
            "      \"isPossible\": false,\n" +
            "      \"level\": 0\n" +
            "    },\n" +
            "    \"humidity\": {\n" +
            "      \"isPossible\": false,\n" +
            "      \"level\": 5\n" +
            "    }\n" +
            "  }\n" +
            "}"
    val jsonDeserializedLog = Json.decodeFromString<DailyLog>(jsonBodyLog)


    //Irritation values
    const val irritationOverallValue = 8
    val irritationZones = listOf("IrritationZone")
    val irritation = Irritation(irritationOverallValue, irritationZones)
    const val irritationOverallValue2 = 5
    val irritationZones2 = listOf("IrritationZone", "IrritationZone2")
    val irritationEdited = Irritation(irritationOverallValue2, irritationZones2)

    //AdditionalData values
    const val adStress = 10
    private const val adWeatherHumidity = 0
    private const val adWeatherTemperature = 5
    private const val adTraveled = true
    private const val adCity = "Madrid"
    val adAlcohol = AlcoholLevel.Some
    val adBeerTypes = listOf("Ale")
    val weather = AdditionalData.Weather(adWeatherHumidity, adWeatherTemperature)
    val travel = AdditionalData.Travel(adTraveled, adCity)
    val additionalData = AdditionalData(adStress, weather, travel, adAlcohol, adBeerTypes)
    const val adStress2 = 3
    private const val adWeatherHumidity2 = 1
    private const val adWeatherTemperature2 = 2
    private const val adTraveled2 = false
    private const val adCity2 = ""
    val adAlcohol2 = AlcoholLevel.FewWine
    val adBeerTypes2 = emptyList<String>()
    val weather2 = AdditionalData.Weather(adWeatherHumidity2, adWeatherTemperature2)
    val travel2 = AdditionalData.Travel(adTraveled2, adCity2)
    val additionalDataEdited = AdditionalData(
        adStress2,
        weather2,
        travel2,
        adAlcohol2,
        adBeerTypes2
    )

    //Log data
    const val date = "31-12-2022"
    const val date2 = "01-01-2023"
    const val date3 = "02-01-2023"
    const val date4 = "03-01-2023"
    const val date5 = "04-01-2023"
    const val date6 = "05-01-2023"

    const val userId = "userId"
    val foodList = listOf("meat", "fish")
    val foodList2 = listOf("strawberry", "fish", "banana")
    val idValues = LogIdValues(dayDate = date, userId = userId)
    val log = DailyLog(date, foodList, irritation, additionalData)
    val log2 = DailyLog(date2, foodList2, irritationEdited, additionalDataEdited)
    val logList = listOf(log, log2)
    val bigLogList = listOf(
        log,
        log2,
        log.copy(date = date3),
        log.copy(date = date4),
        log.copy(date = date5),
        log.copy(date = date6)
    )

    val logEdited = DailyLog(date, foodList2, irritationEdited, additionalDataEdited)


    //Stats

    val emptyStats = StatsDto()
    val statsAlcohol = StatsAlcohol(true, adBeerTypes.first())
    val statsStress = StatsStress(false, adStress)
    val statsTravel = StatsTravel(true, adCity)
    val statsTemperature = StatsWeather.StatsTemperature(false, adWeatherHumidity)
    val statsHumidity = StatsWeather.StatsHumidity(false, adWeatherTemperature)
    val stats = StatsDto(
        relevantLogs = 2,
        enoughData = false,
        dietaryCauses = foodList,
        mostAffectedZones = irritationZones,
        alcohol = statsAlcohol,
        stress = statsStress,
        travel = statsTravel,
        weather = StatsWeather(statsTemperature, statsHumidity)
    )

    val foodMap = mapOf(
        "meat" to 1,
        "strawberry" to 1,
        "fish" to 2,
        "banana" to 1
    )

    val zonesMap = mapOf(
        "IrritationZone" to 2,
        "IrritationZone2" to 1
    )

}
