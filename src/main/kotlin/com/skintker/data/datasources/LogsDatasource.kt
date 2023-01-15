package com.skintker.data.datasources

import com.skintker.data.dto.logs.AdditionalData
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.logs.Irritation
import com.skintker.domain.model.LogIdValues

interface LogsDatasource {
    suspend fun getAllLogs(userId: String): List<DailyLog>

    suspend fun getLog(idValues: LogIdValues): DailyLog?

    suspend fun addNewLog(
        idValues: LogIdValues,
        foodValue: List<String>,
        irritationValue: Irritation,
        additionalDataValue: AdditionalData
    ): Int

    suspend fun editLog(
        idValues: LogIdValues,
        foodValue: List<String>,
        irritationValue: Irritation,
        additionalDataValue: AdditionalData
    ): Boolean

    suspend fun deleteLog(idValues: LogIdValues): Boolean

    suspend fun deleteAllLogs(userId: String): Boolean

}