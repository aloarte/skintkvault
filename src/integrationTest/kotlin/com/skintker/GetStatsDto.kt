package com.skintker

import com.skintker.data.dto.stats.StatsDto
import kotlinx.serialization.Serializable

@Serializable
data class GetStatsDto(
    val statusCode : Int,
    val content : ReportsContent,
) {
    @Serializable
    data class ReportsContent(
        val type: String,
        val stats: StatsDto
    )
}
