package com.skintker.data.dto.stats

import kotlinx.serialization.Serializable

@Serializable
data class StatsAlcohol(
    val isPossible: Boolean = false,
    val beerType: String? = null
)

