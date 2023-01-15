package com.skintker.data.dto.logs

import kotlinx.serialization.Serializable

@Serializable
data class Irritation(
    val overallValue: Int,
    val zoneValues: List<String>
)