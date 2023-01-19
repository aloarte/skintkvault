package com.skintker

import com.skintker.data.dto.logs.DailyLog
import kotlinx.serialization.Serializable

@Serializable
data class GetAllReportsDto(
    val statusCode : Int,
    val content : ReportsContent,
) {
    @Serializable
    data class ReportsContent(
        val type: String,
        val logList: List<DailyLog>,
        val count:Int
    )
}