package com.skintker.data.datasources.impl

import com.skintker.data.datasources.StatsDatasource
import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.stats.*
import com.skintker.data.increaseValue
import com.skintker.data.processors.StatsDataProcessor

class StatsDatasourceImpl(private val statsProcessor: StatsDataProcessor) : StatsDatasource {

    companion object {
        const val MIN_LOGS = 10
        private const val STRESS_THRESHOLD = 7
        private const val FOOD_AMOUNT_THRESHOLD = 0.7f
        private const val ZONES_AMOUNT_THRESHOLD = 0.7f
        private const val HUMIDITY_AMOUNT_THRESHOLD = 0.7f
        private const val TEMPERATURE_AMOUNT_THRESHOLD = 0.7f
        private const val TRAVEL_AMOUNT_THRESHOLD = 0.7f
        private const val STRESS_AMOUNT_THRESHOLD = 0.7f
        private const val BEERS_AMOUNT_THRESHOLD = 0.7f
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