package com.skintker.data.dto.logs

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalData(
    val stressLevel: Int,
    val weather: Weather,
    val travel: Travel,
    val alcohol: Alcohol
) {
    @Serializable
    data class Weather(val humidity: Int, val temperature: Int)

    @Serializable
    data class Travel(val traveled: Boolean, val city: String)

    @Serializable
    data class Alcohol(
        val level: AlcoholLevel,
        val beers: List<String> = emptyList(),
        val wines: List<String> = emptyList(),
        val distilledDrinks: List<String> = emptyList()
    )
}
