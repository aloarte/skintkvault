package com.skintker.data.datasources.impl

import com.skintker.data.BEERS_AMOUNT_THRESHOLD
import com.skintker.data.FOOD_AMOUNT_THRESHOLD
import com.skintker.data.HUMIDITY_AMOUNT_THRESHOLD
import com.skintker.data.STRESS_AMOUNT_THRESHOLD
import com.skintker.data.STRESS_THRESHOLD
import com.skintker.data.TEMPERATURE_AMOUNT_THRESHOLD
import com.skintker.data.TRAVEL_AMOUNT_THRESHOLD
import com.skintker.data.ZONES_AMOUNT_THRESHOLD
import com.skintker.data.datasources.StatsDatasource
import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.components.StatsDataProcessor
import com.skintker.data.dto.stats.StatsDto
import com.skintker.data.dto.stats.StatsWeather
import com.skintker.data.increaseValue

class StatsDatasourceImpl(private val statsProcessor: StatsDataProcessor) : StatsDatasource {

    companion object {
        const val MIN_LOGS = 10
    }

    override fun calculateStats(logList: List<DailyLog>): StatsDto {

        val foodMap = mutableMapOf<String, Int>()
        val zonesMap = mutableMapOf<String, Int>()
        val stressMap = mutableMapOf<Int, Int>()
        val traveledMap = mutableMapOf<Boolean, Int>()
        val traveledCityMap = mutableMapOf<String, Int>()
        val temperatureMap = mutableMapOf<Int, Int>()
        val humidityCityMap = mutableMapOf<Int, Int>()
        val alcoholTypeMap = mutableMapOf<AlcoholLevel, Int>()
        val beerTypeMap = mutableMapOf<String, Int>()

        logList.forEach { log ->
            log.foodList.forEach { food ->
                foodMap.increaseValue(food)
            }
            log.irritation.zoneValues.forEach { irritatedZone ->
                zonesMap.increaseValue(irritatedZone)
            }
            log.additionalData.let { additionalData ->
                stressMap.increaseValue(additionalData.stressLevel)
                traveledMap.increaseValue(additionalData.travel.traveled)
                traveledCityMap.increaseValue(additionalData.travel.city)
                temperatureMap.increaseValue(additionalData.weather.temperature)
                humidityCityMap.increaseValue(additionalData.weather.humidity)
                alcoholTypeMap.increaseValue(additionalData.alcoholLevel)
                additionalData.beerTypes.forEach { beerType ->
                    beerTypeMap.increaseValue(beerType)
                }

            }
        }

        return StatsDto(
            enoughData = logList.size >= MIN_LOGS,
            relevantLogs = logList.size,
            dietaryCauses = statsProcessor.getFromItemList(foodMap, FOOD_AMOUNT_THRESHOLD),
            mostAffectedZones = statsProcessor.getFromItemList(zonesMap, ZONES_AMOUNT_THRESHOLD),
            alcohol = statsProcessor.getFromAlcohol(beerTypeMap, alcoholTypeMap, BEERS_AMOUNT_THRESHOLD),
            stress = statsProcessor.getFromStress(stressMap, STRESS_THRESHOLD, STRESS_AMOUNT_THRESHOLD),
            travel = statsProcessor.getFromTravel(traveledMap, traveledCityMap, TRAVEL_AMOUNT_THRESHOLD),
            weather = StatsWeather(
                statsProcessor.getFromWeatherTemperature(temperatureMap, TEMPERATURE_AMOUNT_THRESHOLD),
                statsProcessor.getFromWeatherHumidity(humidityCityMap, HUMIDITY_AMOUNT_THRESHOLD)
            )
        )
    }
}
