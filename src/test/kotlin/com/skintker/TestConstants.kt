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
                "    \"alcoholLevel\": \"None\",\n" +
                "    \"beerTypes\": [\n" +
                "      \"Ale\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"foodList\": [\n" +
                "    \"Meat\",\n" +
                "    \"Blue fish\"\n" +
                "  ]\n" +
                "}"



    const val jsonStats = "{\n" +
            "  \"dietaryCauses\": [\n" +
            "    \"Meat\",\n" +
            "    \"Blue fish\"\n" +
            "  ],\n" +
            "  \"mostAffectedZones\": [\n" +
            "    \"IrritationZone\"\n" +
            "  ],\n" +
            "  \"alcohol\": {\n" +
            "    \"isPossible\": true\n" +
            "  },\n" +
            "  \"stress\": true,\n" +
            "  \"travel\": {\n" +
            "    \"isPossible\": true,\n" +
            "    \"city\": \"Madrid\"\n" +
            "  },\n" +
            "  \"weather\": {\n" +
            "    \"temperature\": {\n" +
            "      \"level\": 0\n" +
            "    },\n" +
            "    \"humidity\": {\n" +
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
    val irritationZones2 = listOf("Ears", "Eyelid","Arms","Wrists")
    const val irritationOverallValue3 = 7
    val irritationZones3 = listOf("Wrists", "Ears")
    val irritation3 = Irritation(irritationOverallValue3, irritationZones3)
    const val irritationOverallValue4 = 2
    val irritationZones4 = listOf("Shoulders", "Chest")
    val irritation4 = Irritation(irritationOverallValue4, irritationZones4)

    val irritationEdited = Irritation(irritationOverallValue2, irritationZones2)

    //AdditionalData values
    const val adStress = 10
    private const val adWeatherHumidity = 0
    private const val adWeatherTemperature = 5
    private const val adTraveled = true
    private const val adCity = "Madrid"
    val adAlcohol = AlcoholLevel.None
    val adBeerTypes = listOf("Ale")
    val weather = AdditionalData.Weather(adWeatherHumidity, adWeatherTemperature)
    val travel = AdditionalData.Travel(adTraveled, adCity)
    val additionalData = AdditionalData(adStress, weather, travel, adAlcohol, adBeerTypes)
    const val adStress2 = 3
    private const val adWeatherHumidity2 = 1
    private const val adWeatherTemperature2 = 2
    private const val adTraveled2 = false
    private const val adCity2 = ""
    val adAlcohol2 = AlcoholLevel.Wine
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
    const val userToken = "userToken"

    val foodList = listOf("Meat", "Blue fish")
    val foodList2 = listOf("Strawberry", "Blue fish", "Banana")
    val foodList3 = listOf("Meat", "Pineapple", "Citrus","Pickles")
    val foodList4 = listOf("Strawberry", "Blue fish", "Banana")

    val idValues = LogIdValues(dayDate = date, userId = userId)
    val log = DailyLog(date, foodList, irritation, additionalData)
    val log2 = DailyLog(date2, foodList2, irritationEdited, additionalDataEdited)
    val log3 = DailyLog(date3, foodList3, irritation3, additionalData)
    val log4 = DailyLog(date3, foodList4, irritation4, additionalData)

    val logList = listOf(log, log2,log3,log4)
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

    val statsAlcohol = StatsAlcohol(true)
    val statsStress = StatsStress(false, adStress)
    val statsTravel = StatsTravel(true, adCity)
    val statsTemperature = StatsWeather.StatsTemperature(false, adWeatherHumidity)
    val statsHumidity = StatsWeather.StatsHumidity(false, adWeatherTemperature)
    val stats = StatsDto(
        dietaryCauses = foodList,
        mostAffectedZones = irritationZones,
        alcohol = statsAlcohol,
        stress = true,
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
