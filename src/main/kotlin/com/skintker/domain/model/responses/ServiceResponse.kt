package com.skintker.domain.model.responses

import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponse(val statusCode: Int, val statusMessage: String? = null, val content: DataResponses? = null)
