package com.skintker.data.dto.stats

import com.skintker.data.dto.logs.AlcoholLevel
import kotlinx.serialization.Serializable

@Serializable
data class StatsAlcohol(
    val isPossible: Boolean = false,
    val type: AlcoholLevel = AlcoholLevel.None,
    val suspiciousBeers: List<String> = emptyList(),
    val suspiciousWines: List<String> = emptyList(),
    val suspiciousDrinks: List<String> = emptyList()

)

