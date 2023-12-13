package com.skintker.data.components

import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.increaseValue
import com.skintker.data.model.LogsStatsData

class StatsDataInitializer {

    private fun getFoodReferenceList() = listOf(
        "Meat",
        "Seafood",
        "Blue fish",
        "White fish",
        "Pineapple",
        "Strawberries",
        "Bananas",
        "Citrus",
        "Tomato",
        "Avocado",
        "Eggplant",
        "Zucchini",
        "Pumpkin",
        "Peas",
        "Mushrooms",
        "Eggs",
        "Hot spices",
        "Nuts",
        "Chocolate",
        "Sweets",
        "Dairy products",
        "Fermented dairy",
        "Bread",
        "Pasta",
        "Pickles",
        "Canned legumes",
        "Canned vegetables",
        "Canned fish",
        "Inlay",
        "Sauces",
        "Soy derivatives"
    )

    private fun getZonesReferenceList() = listOf(
        "Ears", "Eyelid", "Cheeks", "Lips", "Neck",
        "Shoulders", "Arms", "Wrists", "Hands", "Legs",
        "Chest", "Back"
    )

    private fun getBeersReferenceList() = listOf(
        "Wheat", "Stout", "Porter", "Lager", "Dark Lager",
        "Brown Ale", "Pale Ale/IPA", "Belgian-Style Ale", "Sour Ale", "Other"
    )

    private fun getWinesReferenceList() = listOf(
        "Red", "White", "Rose", "Sparkling", "Vermouth", "Other"
    )

    private fun getDistilledDrinksReferenceList() = listOf(
        "Gin", "Vodka", "Rum", "Whiskey", "Tequila",
        "Brandy", "Other"
    )

    private fun initializeMap(referenceList: List<String>, size: Int): MutableMap<String, List<Boolean>> {
        val map = mutableMapOf<String, List<Boolean>>()
        referenceList.forEach {
            map[it] = List(size) { false }
        }
        return map
    }

    fun prepareData(logList: List<DailyLog>): LogsStatsData {
        val foodsLevels = initializeMap(getFoodReferenceList(), logList.size)
        val zonesLevels = initializeMap(getZonesReferenceList(), logList.size)
        val irritationLevels = mutableListOf<Int>()
        val stressLevels = mutableListOf<Int>()
        val temperatureLevels = mutableListOf<Int>()
        val humidityLevels = mutableListOf<Int>()
        val traveledLevels = mutableListOf<Boolean>()
        val traveledCityMap = mutableMapOf<String, Int>()
        val alcoholTypeLevels = mutableListOf<AlcoholLevel>()
        val beersLevels = initializeMap(getBeersReferenceList(), logList.size)
        val wineLevels = initializeMap(getWinesReferenceList(), logList.size)
        val distilledDrinksLevels = initializeMap(getDistilledDrinksReferenceList(), logList.size)

        logList.forEachIndexed { index, log ->
            log.foodList.forEach { food ->
                foodsLevels.increaseValue(food, index)
            }
            log.irritation.let {
                it.zoneValues.forEach { irritatedZone ->
                    zonesLevels.increaseValue(irritatedZone, index)
                }
                irritationLevels.add(index, it.overallValue)

            }
            log.additionalData.let {
                stressLevels.add(index, it.stressLevel)
                temperatureLevels.add(index, it.weather.temperature)
                humidityLevels.add(index, it.weather.humidity)
                traveledLevels.add(index, it.travel.traveled)
                traveledCityMap.increaseValue(it.travel.city)
                alcoholTypeLevels.add(index, it.alcohol.level)
                it.alcohol.beers.forEach { beerType ->
                    beersLevels.increaseValue(beerType, index)
                }
                it.alcohol.wines.forEach { beerType ->
                    wineLevels.increaseValue(beerType, index)
                }
                it.alcohol.distilledDrinks.forEach { beerType ->
                    distilledDrinksLevels.increaseValue(beerType, index)
                }
            }
        }

        return LogsStatsData(
            foodsLevels,
            zonesLevels,
            irritationLevels,
            stressLevels,
            temperatureLevels,
            humidityLevels,
            traveledLevels,
            traveledCityMap,
            alcoholTypeLevels,
            beersLevels
        )
    }

}
