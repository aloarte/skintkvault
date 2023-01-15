package com.skintker.domain.model.responses

import com.skintker.data.dto.logs.DailyLog
import kotlinx.serialization.Serializable

@Serializable
sealed class DataResponses


@Serializable
sealed class LogResponse() : DataResponses()

@Serializable
class LogListResponse(var logList: List<DailyLog>) : LogResponse()


