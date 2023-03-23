package com.skintker.domain.repository

import com.skintker.data.dto.stats.StatsDto

interface StatsRepository {

    suspend fun calculateUserStats(userId: String, statsThreshold: Int): StatsDto?

}
