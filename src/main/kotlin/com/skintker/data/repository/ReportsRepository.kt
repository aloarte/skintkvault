package com.skintker.data.repository

import com.skintker.data.dto.DailyLog
import com.skintker.model.SaveReportStatus

interface ReportsRepository {
    suspend fun saveReport(userId: String, report: DailyLog): SaveReportStatus

    suspend fun getReports(userId: String): List<DailyLog>

    suspend fun deleteReports(userId: String): Boolean

}