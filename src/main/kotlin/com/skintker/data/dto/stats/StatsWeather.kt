package com.skintker.data.dto.stats

import kotlinx.serialization.Serializable

@Serializable
data class StatsWeather(
    val temperature: StatsTemperature = StatsTemperature(),
    val humidity: StatsHumidity = StatsHumidity()
) {
    @Serializable
    data class StatsTemperature(
        val isPossible: Boolean = false,
        val level: Int = -1
    )

    @Serializable
    data class StatsHumidity(
        val isPossible: Boolean = false,
        val level: Int = -1
    )

}

