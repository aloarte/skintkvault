package com.skintker.data.db.logs

import com.skintker.data.Constants.DDBB_SIZE_CITY
import com.skintker.data.Constants.DDBB_SIZE_ENUM
import com.skintker.data.Constants.DDBB_SIZE_LIST
import org.jetbrains.exposed.dao.id.IntIdTable

object AdditionalDataTable : IntIdTable(){
    val stressLevel = integer("stressLevel")
    val weatherHumidity = integer("weatherHumidity")
    val weatherTemperature = integer("weatherTemperature")
    val traveled = bool("traveled")
    val travelCity = varchar("travelCity",DDBB_SIZE_CITY)
    val alcoholLevel = varchar("alcoholLevel",DDBB_SIZE_ENUM)
    val beerTypes = varchar("beerTypes", DDBB_SIZE_LIST)
}
