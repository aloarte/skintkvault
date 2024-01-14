package com.skintker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(val email: String)