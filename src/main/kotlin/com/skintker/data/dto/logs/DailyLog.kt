package com.skintker.data.dto.logs

import kotlinx.serialization.Serializable

@Serializable
data class DailyLog(
    val date: String,
    val foodList: List<String>,
    val irritation: Irritation,
    val additionalData: AdditionalData
)
