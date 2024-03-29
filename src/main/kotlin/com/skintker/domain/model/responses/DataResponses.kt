package com.skintker.domain.model.responses

import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.stats.StatsDto
import com.skintker.domain.model.UserReturnType
import kotlinx.serialization.Serializable

@Serializable
sealed class DataResponses

@Serializable
class LogListResponse(
    var logList: List<DailyLog>,
    var count:Int
) : DataResponses()

@Serializable
class StatsResponse(var stats: StatsDto?) : DataResponses()

@Serializable
class UserResponse(var result: UserReturnType) : DataResponses()


