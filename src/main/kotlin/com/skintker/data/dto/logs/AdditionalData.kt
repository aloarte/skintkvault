package com.skintker.data.dto.logs

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalData(
    val stressLevel: Int,
    val weather: Weather,
    val travel: Travel,
    val alcoholLevel: AlcoholLevel,
    val beerTypes: List<String> = emptyList()
) {
    @Serializable
    data class Weather(val humidity: Int, val temperature: Int)

    @Serializable
    data class Travel(val traveled: Boolean, val city: String)
}
