package com.skintker.data.responses

import com.skintker.data.dto.DailyLog
import kotlinx.serialization.Serializable
import kotlin.collections.List

@Serializable
data class ServiceResponse(val statusCode: Int, val statusMessage: String? = null, val content: DataResponses? = null)