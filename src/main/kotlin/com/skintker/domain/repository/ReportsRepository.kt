package com.skintker.domain.repository

import com.skintker.data.dto.logs.DailyLog
import com.skintker.domain.model.LogIdValues
import com.skintker.domain.model.SaveReportStatus

interface ReportsRepository {
    suspend fun getReports(userId: String): List<DailyLog>

    suspend fun deleteReport(idValues: LogIdValues): Boolean

    suspend fun deleteReports(userId: String): Boolean

    suspend fun saveReport(userId: String, report: DailyLog): SaveReportStatus

}
