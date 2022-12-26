package com.skintker.manager.impl

import com.skintker.manager.DatabaseManager
import com.skintker.model.DailyLog

class DatabaseManagerImpl: DatabaseManager {
    override fun saveReport(userToken: String, report: DailyLog) {
    }

    override fun getReports(userToken: String): List<DailyLog> {
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
}