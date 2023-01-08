package com.skintker.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Irritation(
    val overallValue: Int,
    val zoneValues: List<String>?= emptyList()
)