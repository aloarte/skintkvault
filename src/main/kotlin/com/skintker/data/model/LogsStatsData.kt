package com.skintker.data.model

import com.skintker.data.dto.logs.AlcoholLevel

data class LogsStatsData(
    val foodsLevels: Map<String, List<Boolean>> = mapOf(),
    val zonesLevels: Map<String, List<Boolean>> = mapOf(),
    val irritationLevels: List<Int> = emptyList(),
    val stressLevels: List<Int> = emptyList(),
    val temperatureLevels: List<Int> = emptyList(),
    val humidityLevels: List<Int> = emptyList(),
    val traveledLevels: List<Boolean> = emptyList(),
    val traveledCityMap: Map<String, Int> = mapOf(),
    val alcoholTypeMap: List<AlcoholLevel> = emptyList(),
    val beersLevels: Map<String, List<Boolean>> = mapOf(),
    val winesLevels: Map<String, List<Boolean>> = mapOf(),
    val drinksLevels: Map<String, List<Boolean>> = mapOf()

)