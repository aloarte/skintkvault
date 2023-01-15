package com.skintker.data.dto.stats

import kotlinx.serialization.Serializable

@Serializable
data class StatsStress(
    val isPossible: Boolean = false,
    val level: Int = -1
)

