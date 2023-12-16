package com.skintker.data.dto.stats

import kotlinx.serialization.Serializable

@Serializable
data class StatsDto(
    val dietaryCauses: List<String> = emptyList(),
    val mostAffectedZones: List<String> = emptyList(),
    val alcohol: StatsAlcohol = StatsAlcohol(),
    val stress: Boolean =false,
    val travel: StatsTravel = StatsTravel(),
    val weather: StatsWeather = StatsWeather()
)
