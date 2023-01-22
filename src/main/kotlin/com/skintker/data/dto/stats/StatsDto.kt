package com.skintker.data.dto.stats

import kotlinx.serialization.Serializable

@Serializable
data class StatsDto(
    val enoughData: Boolean = false,
    val relevantLogs:Int=-1,
    val dietaryCauses: List<String> = emptyList(),
    val mostAffectedZones: List<String> = emptyList(),
    val alcohol: StatsAlcohol = StatsAlcohol(),
    val stress: StatsStress = StatsStress(),
    val travel: StatsTravel = StatsTravel(),
    val weather: StatsWeather = StatsWeather()
)
