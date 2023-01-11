package com.skintker.domain.repository

import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.dto.DailyLog
import com.skintker.domain.model.LogIdValues
import com.skintker.domain.model.SaveReportStatus

class ReportsRepositoryImpl(private val logsDao: LogsDatasource) : ReportsRepository {

    override suspend fun getReports(userId: String): List<DailyLog> {
        return logsDao.getAllLogs(userId)
    }

    override suspend fun deleteReport(idValues: LogIdValues): Boolean {
        return logsDao.deleteLog(idValues)
    }

    override suspend fun deleteReports(userId: String): Boolean {
        return logsDao.deleteAllLogs(userId)
    }

    override suspend fun saveReport(userId: String, report: DailyLog): SaveReportStatus {
        val idValues = LogIdValues(dayDate = report.date, userId = userId)
        //First try to get the log to know if the log must be edited or added
        val result = if (logsDao.getLog(idValues) == null) {
            Pair(true, logsDao.addNewLog(idValues, report.foodList, report.irritation, report.additionalData)>0)
        } else {
            Pair(false, logsDao.editLog(idValues, report.foodList, report.irritation, report.additionalData))
        }

        return when {
            result.first && result.second -> SaveReportStatus.Saved
            result.first && !result.second -> SaveReportStatus.SavingFailed
            !result.first && result.second -> SaveReportStatus.Edited
            else -> SaveReportStatus.EditingFailed
        }
    }

}