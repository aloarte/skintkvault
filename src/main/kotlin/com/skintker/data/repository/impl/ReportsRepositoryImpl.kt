package com.skintker.data.repository.impl

import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.dto.DailyLog
import com.skintker.model.LogIdValues
import com.skintker.model.SaveReportStatus

class ReportsRepositoryImpl(private val logsDao: LogsDatasource): ReportsRepository {
    override suspend fun saveReport(userId: String, report: DailyLog): SaveReportStatus {
        //TODO: Check that the userId is not empty and that the date from the daily log make sense. Otherwise return a new SaveReportStatus
        val idValues = LogIdValues(dayDate = report.date,userId = userId)
        val result = if(logsDao.getLog(idValues)==null){
            Pair(true,logsDao.addNewLog(idValues,report.foodList!!)!=null)
        }else{
            Pair(false,logsDao.editLog(idValues,report.foodList!!))
        }
        //Todo: review !!

        return when{
            result.first && result.second -> SaveReportStatus.Saved
            result.first && !result.second -> SaveReportStatus.SavingFailed
            !result.first && result.second -> SaveReportStatus.Edited
            else -> SaveReportStatus.EditingFailed
        }
    }

    override suspend fun getReports(userId: String): List<DailyLog> {
        return listOf(
            DailyLog(
                date= "date",
                irritation = DailyLog.Irritation(
                    overallValue=3,
                    zoneValues = listOf("Wrist","Chest")
                ),
                additionalData = DailyLog.AdditionalData(
                    stressLevel=10,
                    weather= DailyLog.AdditionalData.Weather(humidity = 5, temperature = 5),
                    travel = DailyLog.AdditionalData.Travel(false,"Madrid"),
                    alcoholLevel = DailyLog.AlcoholLevel.FewWine
                ),
                foodList = listOf("Bread","Milk","Eggs"))
        )
    }

    override suspend fun deleteReports(userId: String): Boolean {
        return true
    }
}