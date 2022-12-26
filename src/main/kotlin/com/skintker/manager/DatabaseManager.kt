package com.skintker.manager

import com.skintker.model.DailyLog

interface DatabaseManager {
    fun saveReport(userToken:String, report: DailyLog)
}