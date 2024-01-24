package com.skintker

import com.skintker.data.dto.logs.AdditionalData
import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.logs.Irritation
import com.skintker.data.dto.stats.StatsAlcohol
import com.skintker.data.dto.stats.StatsDto
import com.skintker.data.dto.stats.StatsTravel
import com.skintker.data.dto.stats.StatsWeather
import com.skintker.domain.model.LogIdValues
import kotlinx.serialization.json.Json

object TestConstants {
    const val jsonBodyLog = "{\n" +
            "  \"date\": \"31-12-2022\",\n" +
            "  \"foodList\": [\n" +
            "    \"Meat\",\n" +
            "    \"Blue fish\"\n" +
            "  ],\n" +
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
            "    \"alcohol\": {\n" +
            "      \"level\": \"Beer\",\n" +
            "      \"beers\": [\n" +
            "        \"Ale\"\n" +
            "      ],\n" +
            "      \"wines\": [\n" +
            "        \"White\"\n" +
            "      ],\n" +
            "      \"distilledDrinks\": [\n" +
            "        \"Gin\"\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
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
    val irritation = Irritation(8, listOf("IrritationZone"))
    val irritation2 = Irritation(5, listOf("Ears", "Eyelid", "Arms", "Wrists"))
    val irritation3 = Irritation(7, listOf("Wrists", "Ears"))
    val irritation4 = Irritation(2, listOf("Shoulders", "Chest"))
    val irritationEdited = Irritation(5, listOf("Ears", "Eyelid", "Arms", "Wrists"))

    //AdditionalData values
    val adBeerTypes = listOf("Ale")
    val adWineTypes = listOf("White")
    val addistilledTypes = listOf("Gin")
    val adAlcohol = AdditionalData.Alcohol(
        AlcoholLevel.Beer,
        adBeerTypes,
        adWineTypes,
        addistilledTypes
    )
    val weather = AdditionalData.Weather(0, 5)
    val travel = AdditionalData.Travel(true, "Madrid")
    val additionalData = AdditionalData(10, weather, travel, adAlcohol)

    const val adStress2 = 3
    private const val adWeatherHumidity2 = 1
    private const val adWeatherTemperature2 = 2
    private const val adTraveled2 = false
    private const val adCity2 = ""
    val adBeerTypes2 = emptyList<String>()
    val adWineTypes2 = emptyList<String>()
    val adDistilledTypes2 = emptyList<String>()
    val adAlcohol2 = AdditionalData.Alcohol(
        AlcoholLevel.Wine,
        adBeerTypes2,
        adWineTypes2,
        adDistilledTypes2
    )


    val weather2 = AdditionalData.Weather(adWeatherHumidity2, adWeatherTemperature2)
    val travel2 = AdditionalData.Travel(adTraveled2, adCity2)
    val additionalDataEdited = AdditionalData(
        adStress2,
        weather2,
        travel2,
        adAlcohol2

    )

    //Log data
    const val date = "31-12-2022"
    const val date2 = "01-01-2023"
    const val date3 = "02-01-2023"
    const val date4 = "03-01-2023"
    const val date5 = "04-01-2023"
    const val date6 = "05-01-2023"
    const val userId = "userId"
    const val userEmail = "user@email.com"
    const val userToken = "userToken"

    const val jsonBodyDeleteMail = "{\"email\": $userEmail}"

    const val jsonBodyAddUser = "{\"userId\": $userId}"

    const val jsonBodyAddUserMalformed = "{\"userI $userId}"

    val foodList = listOf("Meat", "Blue fish")
    val foodList2 = listOf("Strawberry", "Blue fish", "Banana")
    val foodList3 = listOf("Meat", "Pineapple", "Citrus", "Pickles")
    val foodList4 = listOf("Strawberry", "Blue fish", "Banana")

    val idValues = LogIdValues(dayDate = date, userId = userId)
    val idValues2 = LogIdValues(dayDate = date2, userId = userId)

    val log = DailyLog(date, foodList, irritation, additionalData)
    val log2 = DailyLog(date2, foodList2, irritationEdited, additionalDataEdited)
    val log3 = DailyLog(date3, foodList3, irritation3, additionalData)
    val log4 = DailyLog(date3, foodList4, irritation4, additionalData)

    val logList = listOf(log, log2, log3, log4)
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
    val statsTravel = StatsTravel(true, "Madrid")
    val statsTemperature = StatsWeather.StatsTemperature(false, 0)
    val statsHumidity = StatsWeather.StatsHumidity(false, 5)
    val stats = StatsDto(
        dietaryCauses = foodList,
        mostAffectedZones = listOf("IrritationZone"),
        alcohol = statsAlcohol,
        stress = true,
        travel = statsTravel,
        weather = StatsWeather(statsTemperature, statsHumidity)
    )

}
