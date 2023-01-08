package com.skintker.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DailyLog(
    val date: String,
    val irritation: Irritation? = null,
    val additionalData: AdditionalData? = null,
    val foodList: List<String>?=null
)