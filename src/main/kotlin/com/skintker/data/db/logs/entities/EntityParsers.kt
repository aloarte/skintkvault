package com.skintker.data.db.logs.entities

import com.skintker.data.dto.logs.AdditionalData
import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.logs.Irritation

object EntityParsers {

    fun logEntityToBo(entity: LogsEntity) = DailyLog(
        date = entity.dayDate,
        foodList = entity.foodList.split(","),
        irritation = irritationEntityToBo(entity.irritation),
        additionalData = additionalDataEntityToBo(entity.additionalData)
    )

    fun irritationEntityToBo(entity: IrritationEntity) = Irritation(
        overallValue = entity.value, zoneValues = entity.zoneValues.split(",")
    )

    fun additionalDataEntityToBo(entity: AdditionalDataEntity) = AdditionalData(
        stressLevel = entity.stressLevel,
        weather = AdditionalData.Weather(
            humidity = entity.weatherHumidity,
            temperature = entity.weatherTemperature
        ), travel = AdditionalData.Travel(
            traveled = entity.traveled,
            city = entity.travelCity
        ),
        alcohol = AdditionalData.Alcohol(
            level = AlcoholLevel.valueOf(entity.alcoholLevel),
            beers = entity.beers.split(","),
            wines = entity.wines.split(","),
            distilledDrinks = entity.distilledDrinks.split(",")
        )

    )
}
