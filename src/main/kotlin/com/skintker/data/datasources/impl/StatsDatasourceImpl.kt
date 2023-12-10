package com.skintker.data.datasources.impl

import com.skintker.data.ALCOHOLIC_DRINKS_AMOUNT_THRESHOLD
import com.skintker.data.FOOD_AMOUNT_THRESHOLD
import com.skintker.data.STRESS_THRESHOLD
import com.skintker.data.TRAVEL_AMOUNT_THRESHOLD
import com.skintker.data.ZONES_AMOUNT_THRESHOLD
import com.skintker.data.components.StatsDataInitializer
import com.skintker.data.datasources.StatsDatasource
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.components.StatsDataProcessor
import com.skintker.data.dto.stats.StatsDto
import com.skintker.data.dto.stats.StatsWeather
import com.skintker.data.model.InvolvedDataType

class StatsDatasourceImpl(
    private val statsProcessor: StatsDataProcessor,
    private val dataInitializer: StatsDataInitializer
) : StatsDatasource {

    override fun calculateStats(logList: List<DailyLog>): StatsDto {
        val statsData = dataInitializer.prepareData(logList)

        return StatsDto(
            dietaryCauses = statsProcessor.getInvolvedData(InvolvedDataType.Foods, statsData, FOOD_AMOUNT_THRESHOLD),
            mostAffectedZones = statsProcessor.getInvolvedData(
                InvolvedDataType.Zones,
                statsData,
                ZONES_AMOUNT_THRESHOLD
            ),
            alcohol = statsProcessor.isAlcoholInvolved(statsData, ALCOHOLIC_DRINKS_AMOUNT_THRESHOLD),
            stress = statsProcessor.isStressInvolved(statsData, STRESS_THRESHOLD),
            travel = statsProcessor.isTravelingInvolved(statsData, TRAVEL_AMOUNT_THRESHOLD),
            weather = StatsWeather(
                statsProcessor.isWeatherTemperatureInvolved(statsData),
                statsProcessor.isWeatherHumidityInvolved(statsData)
            )
        ).also {
            println("stats $it")
        }
    }
}

