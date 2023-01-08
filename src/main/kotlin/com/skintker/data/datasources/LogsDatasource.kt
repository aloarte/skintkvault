package com.skintker.data.datasources

import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.DailyLog
import com.skintker.data.dto.Irritation
import com.skintker.model.LogIdValues

interface LogsDatasource {
    suspend fun getAllLogs(userId: String): List<DailyLog>

    suspend fun getLog(idValues: LogIdValues): DailyLog?

    suspend fun addNewLog(
        idValues: LogIdValues,
        food: List<String>? = null,
        irritation: Irritation? = null,
        additionalData: AdditionalData? = null
    ): DailyLog?

    suspend fun editLog(
        idValues: LogIdValues,
        food: List<String>? = null,
        irritation: Irritation? = null,
        additionalData: AdditionalData? = null
    ): Boolean

    suspend fun deleteLog(idValues: LogIdValues): Boolean

    suspend fun deleteAllLogs(userId: String): Boolean

}