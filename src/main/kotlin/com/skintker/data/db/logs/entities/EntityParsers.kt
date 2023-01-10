package com.skintker.data.db.logs.entities

import com.skintker.data.db.logs.Logs
import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.AlcoholLevel
import com.skintker.data.dto.DailyLog
import com.skintker.data.dto.Irritation
import org.jetbrains.exposed.sql.ResultRow

object EntityParsers {


    fun logEntityToBo(entity: LogsEntity) = DailyLog(
        date = entity.dayDate,
        foodList = entity.foodList.split(","),
        irritation = irritationEntityToBo(entity.irritation),
        additionalData = AdditionalData(0,AdditionalData.Weather(1,1), AdditionalData.Travel(true,""),AlcoholLevel.Few)
    )

    fun irritationEntityToBo(entity: IrritationEntity) = Irritation(
        overallValue = entity.value,
        zoneValues = entity.zoneValues.split(",")
    )


}