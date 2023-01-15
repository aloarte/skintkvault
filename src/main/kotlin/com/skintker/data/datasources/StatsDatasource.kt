package com.skintker.data.datasources

import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.stats.StatsDto

interface StatsDatasource {

    fun calculateStats(logList:List<DailyLog>): StatsDto

}