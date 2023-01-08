package com.skintker.data.repository.impl

import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.AlcoholLevel
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.dto.DailyLog
import com.skintker.data.dto.Irritation
import com.skintker.model.LogIdValues
import com.skintker.model.SaveReportStatus

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
        //If the 3 possible fields to modify in the log are null, return an error
        if ((report.foodList == null && report.irritation == null && report.additionalData == null) || userId.isEmpty()) {  //TODO: Improve the userId check
            return SaveReportStatus.BadInput
        }
        val idValues = LogIdValues(dayDate = report.date, userId = userId)
        //First try to get the log to know if the log must be edited or added
        val result = if (logsDao.getLog(idValues) == null) {
            Pair(true, logsDao.addNewLog(idValues, report.foodList, report.irritation, report.additionalData) != null)
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