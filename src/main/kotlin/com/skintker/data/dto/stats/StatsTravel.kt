package com.skintker.data.dto.stats

import kotlinx.serialization.Serializable

@Serializable
data class StatsTravel(
    val isPossible: Boolean = false,
    val city: String? = null
)

