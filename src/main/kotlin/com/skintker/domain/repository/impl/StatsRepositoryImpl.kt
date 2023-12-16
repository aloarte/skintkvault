package com.skintker.domain.repository.impl

import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.datasources.StatsDatasource
import com.skintker.data.dto.stats.StatsDto
import com.skintker.domain.repository.StatsRepository

class StatsRepositoryImpl(private val logsDatasource: LogsDatasource, private val statsDatasource: StatsDatasource) :
    StatsRepository {
    override suspend fun calculateUserStats(userId: String, statsThreshold: Int): StatsDto? {
        val userLogs = logsDatasource.getAllLogs(userId)
        return if (userLogs.isNotEmpty()) {
            statsDatasource.calculateStats(userLogs)
        } else {
            null
        }
    }

}
