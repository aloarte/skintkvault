package com.skintker.data.repository

import com.skintker.data.dto.DailyLog
import com.skintker.model.LogIdValues
import com.skintker.model.SaveReportStatus

interface ReportsRepository {
    suspend fun getReports(userId: String): List<DailyLog>

    suspend fun deleteReport(idValues: LogIdValues): Boolean

    suspend fun deleteReports(userId: String): Boolean

    suspend fun saveReport(userId: String, report: DailyLog): SaveReportStatus

}