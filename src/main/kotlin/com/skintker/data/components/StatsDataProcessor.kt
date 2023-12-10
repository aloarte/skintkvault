package com.skintker.data.components

import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.stats.StatsAlcohol
import com.skintker.data.dto.stats.StatsTravel
import com.skintker.data.dto.stats.StatsWeather
import com.skintker.data.getMaxValue
import com.skintker.data.model.InvolvedDataType
import com.skintker.data.model.LogsStatsData

class StatsDataProcessor(private val statsCalculator: StatisticsCalculator) {

    fun getInvolvedData(involvedDataType: InvolvedDataType, statsData: LogsStatsData, threshold: Double): List<String> =
        getInvolvedData(
            statsData.irritationLevels, when (involvedDataType) {
                InvolvedDataType.Foods -> statsData.foodsLevels
                InvolvedDataType.Zones -> statsData.zonesLevels
                InvolvedDataType.Beers -> statsData.beersLevels
                InvolvedDataType.Wines -> statsData.winesLevels
                InvolvedDataType.Drinks -> statsData.drinksLevels
            }, threshold
        )

    fun isStressInvolved(statsData: LogsStatsData, threshold: Double) =
        statsCalculator.calculateCorrelation(statsData.irritationLevels, statsData.stressLevels)
            .let { !it.isNaN() && it > threshold }

    fun isTravelingInvolved(statsData: LogsStatsData, threshold: Double) = StatsTravel(
        isPossible = statsCalculator.calculateCorrelationBinary(
            statsData.irritationLevels,
            statsData.traveledLevels
        ).let { !it.isNaN() && it > threshold },
        city = statsData.traveledCityMap.getMaxValue()
    )

    fun isWeatherTemperatureInvolved(statsData: LogsStatsData): StatsWeather.StatsTemperature {
        val significantGroup = getSignificantGroup(statsData.irritationLevels, statsData.temperatureLevels)
        return StatsWeather.StatsTemperature(
            isPossible = significantGroup != -1,
            level = significantGroup
        )
    }

    fun isWeatherHumidityInvolved(statsData: LogsStatsData): StatsWeather.StatsHumidity {
        val significantGroup = getSignificantGroup(statsData.irritationLevels, statsData.humidityLevels)
        return StatsWeather.StatsHumidity(
            isPossible = significantGroup != -1,
            level = significantGroup
        )
    }

    fun isAlcoholInvolved(statsData: LogsStatsData, threshold: Double): StatsAlcohol {
        val significantGroup =
            getSignificantGroup(statsData.irritationLevels, statsData.alcoholTypeMap.map { it.value })

        val alcoholIsRelevant = significantGroup != -1
        return StatsAlcohol(
            isPossible = alcoholIsRelevant,
            type = AlcoholLevel.fromValue(significantGroup),
            suspiciousBeers = if (alcoholIsRelevant) {
                getInvolvedData(InvolvedDataType.Beers, statsData, threshold)
            } else {
                emptyList()
            },
            suspiciousWines = if (alcoholIsRelevant) {
                getInvolvedData(InvolvedDataType.Wines, statsData, threshold)
            } else {
                emptyList()
            },
            suspiciousDrinks = if (alcoholIsRelevant) {
                getInvolvedData(InvolvedDataType.Drinks, statsData, threshold)
            } else {
                emptyList()
            }
        )
    }

    private fun getSignificantGroup(continuousData: List<Int>, groupedData: List<Int>): Int {
        //Both datasets must have the same size and the grouped data must have at least 2 groups
        return if (groupedData.toSet().size >= 2 && continuousData.size == groupedData.size) {
            //Group each continuous data into separates lists
            val groups = groupedData.toSet().associateWith { continuousDataLevel ->
                continuousData.filterIndexed { index, _ -> groupedData[index] == continuousDataLevel }
                    .map { it.toDouble() }
                    .toDoubleArray()
            }.filter { it.value.size > 1 } // Avoid those groups with just 1 item

            //If there is any significant group, get the one with the highest average. Also must be more than one group
            if (groups.size > 1 && statsCalculator.isAnyGroupSignificant(groups)) {
                val list = mutableMapOf<Int, Double>()
                groups.forEach {
                    list[it.key] = it.value.average()
                }
                list.entries.maxByOrNull { it.value }?.toPair()?.first ?: -1

            } else -1
        } else {
            -1
        }
    }

    private fun getInvolvedData(
        irritationLevels: List<Int>,
        binaryDataMap: Map<String, List<Boolean>>,
        threshold: Double,
    ): List<String> {
        val zonesCorrelations = statsCalculator.getCorrelations(irritationLevels, binaryDataMap)
        val involvedFoods = mutableListOf<String>()
        zonesCorrelations.forEach { (name, correlation) ->
            if (!correlation.isNaN() && correlation > threshold) {
                involvedFoods.add(name)
            }
        }
        return involvedFoods
    }

}
