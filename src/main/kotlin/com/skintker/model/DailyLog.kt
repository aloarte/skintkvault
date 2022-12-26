package com.skintker.model

import kotlinx.serialization.Serializable

@Serializable
data class DailyLog(
    val date: String,
    val irritation: Irritation? = null,
    val additionalData: AdditionalData? = null,
    val foodList: List<String>?=null
){
    @Serializable
    data class Irritation(
        val overallValue: Int,
        val zoneValues: List<String>?= emptyList()
    )
    @Serializable
    data class AdditionalData(
        val stressLevel: Int,
        val weather: Weather,
        val travel: Travel,
        val alcoholLevel: AlcoholLevel,
        val beerTypes: List<String>? = emptyList()
    ) {
        @Serializable
        data class Weather(val humidity: Int, val temperature: Int)

        @Serializable
        data class Travel(val traveled: Boolean, val city: String)
    }

    enum class AlcoholLevel(val value: Int) {
        None(0), Few(1), FewWine(2), Some(3);

    }
}