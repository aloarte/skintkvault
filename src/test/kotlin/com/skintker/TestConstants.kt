package com.skintker

import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.AlcoholLevel
import com.skintker.data.dto.DailyLog
import com.skintker.data.dto.Irritation
import com.skintker.domain.model.LogIdValues

object TestConstants {
    //Irritation values
    const val irritationOverallValue = 10
    val irritationZones = listOf("IrritationZone")
    val irritation = Irritation(irritationOverallValue, irritationZones)
    const val irritationOverallValue2 = 5
    val irritationZones2 = listOf("IrritationZone2","IrritationZone5")
    val irritationEdited = Irritation(irritationOverallValue2,irritationZones2)

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
    const val userId= "userId"
    val foodList = listOf("meat","fish")
    val foodList2 = listOf("strawberry","fish","banana")
    val idValues = LogIdValues(dayDate = date, userId = userId)
    val log = DailyLog(date, foodList, irritation, additionalData)
    val logEdited = DailyLog(date, foodList2, irritationEdited, additionalDataEdited)


}