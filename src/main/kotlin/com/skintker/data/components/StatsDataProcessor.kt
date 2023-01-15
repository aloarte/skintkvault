package com.skintker.data.components

import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.stats.StatsAlcohol
import com.skintker.data.dto.stats.StatsStress
import com.skintker.data.dto.stats.StatsTravel
import com.skintker.data.dto.stats.StatsWeather
import com.skintker.data.getKeyOfMaxValue
import com.skintker.data.getMaxValue

class StatsDataProcessor {

    /**
     * Parse the given map of <string item , times> returning the list of the most repeated items
     * that get over the given threshold.
     */
    fun getFromItemList(
        itemMap: Map<String, Int>,
        relevantAmountThreshold: Float // Proportion of relevant stressful repetitions
    ): List<String> {
        val returnList = mutableListOf<String>()
        var totalCnt = 0

        for ((_, times) in itemMap) {
            totalCnt += times
        }
        for ((item, times) in itemMap) {
            if (times >= totalCnt * relevantAmountThreshold) {
                returnList.add(item)
            }
        }
        return returnList
    }

    /**
     * Parse the given map of <stress integer value , times> returning a StatsStress using the
     * given thresholds.
     */
    fun getFromStress(
        stressMap: Map<Int, Int>,
        stressThreshold: Int,   //What tells if anything is stressful. If the value is higher to this threshold, it's relevant to consider
        relevantAmountThreshold: Float // Proportion of relevant stressful repetitions
    ): StatsStress {
        if (stressMap.isEmpty()) {
            return StatsStress()
        }
        var totalRepetitions = 0
        var stressfulRepetitions = 0

        stressMap.forEach { (stressLevel, times) ->
            if (stressLevel >= stressThreshold) {
                stressfulRepetitions += times
            }
            totalRepetitions += times
        }
        return StatsStress(
            isPossible = stressfulRepetitions >= (totalRepetitions * relevantAmountThreshold),
            level = stressMap.getMaxValue()
        )
    }

    /**
     * Parse the given maps of <traveled boolean value , times> and <traveled city string value
     * , times> returning a StatsTravel using the given thresholds.
     */
    fun getFromTravel(
        traveledMap: Map<Boolean, Int>,
        traveledCityMap: Map<String, Int>,
        relevantAmountThreshold: Float // Proportion of relevant travel repetitions
    ): StatsTravel {
        if (traveledMap.isEmpty() && traveledCityMap.isEmpty()) {
            return StatsTravel()
        }
        var travelRepetitions = 0
        var totalRepetitions = 0

        traveledMap.forEach { (traveled, times) ->
            if (traveled) {
                travelRepetitions += times
            }
            totalRepetitions += times
        }

        return StatsTravel(
            isPossible = travelRepetitions >= (totalRepetitions * relevantAmountThreshold),
            city = traveledCityMap.getMaxValue()
        )
    }

    /**
     * Parse the given maps of <temperature integer value , times> and <humidity integer string
     * value, times> returning a pair of WeatherCauseBO using the given thresholds.
     */
    fun getFromWeatherTemperature(
        temperatureMap: Map<Int, Int>, relevantAmountThreshold: Float // Proportion of relevant travel repetitions
    ): StatsWeather.StatsTemperature {
        return if (temperatureMap.isEmpty()) {
            StatsWeather.StatsTemperature()
        } else {
            StatsWeather.StatsTemperature(
                isPossible = temperatureMap.getKeyOfMaxValue() > temperatureMap.size * relevantAmountThreshold,
                level = temperatureMap.getMaxValue()
            )
        }
    }

    /**
     * Parse the given maps of <temperature integer value , times> and <humidity integer string
     * value, times> returning a pair of WeatherCauseBO using the given thresholds.
     */
    fun getFromWeatherHumidity(
        humidityMap: Map<Int, Int>,
        relevantAmountThreshold: Float // Proportion of relevant travel repetitions
    ): StatsWeather.StatsHumidity {
        return if (humidityMap.isEmpty()) {
            StatsWeather.StatsHumidity()
        } else {
            StatsWeather.StatsHumidity(
                isPossible = humidityMap.getKeyOfMaxValue() > humidityMap.size * relevantAmountThreshold,
                level = humidityMap.getMaxValue()
            )
        }
    }


    fun getFromAlcohol(
        beerTypesMap: Map<String, Int>,
        alcoholLevelMap: Map<AlcoholLevel, Int>,
        relevantAmountThreshold: Float // Proportion of relevant travel repetitions
    ): StatsAlcohol {
        if (alcoholLevelMap.isEmpty()) {
            return StatsAlcohol()
        }
        var alcoholRepetitions = 0
        var totalRepetitions = 0
        alcoholLevelMap.forEach { (alcoholLevel, times) ->
            if (alcoholLevel != AlcoholLevel.None) {
                alcoholRepetitions += times
            }
            totalRepetitions += times
        }

        return StatsAlcohol(
            isPossible = alcoholRepetitions >= totalRepetitions * relevantAmountThreshold,
            beerType = if (alcoholLevelMap.getMaxValue() == AlcoholLevel.Few || alcoholLevelMap.getMaxValue() == AlcoholLevel.Few) {
                beerTypesMap.getMaxValue()
            } else {
                null
            }
        )
    }


}