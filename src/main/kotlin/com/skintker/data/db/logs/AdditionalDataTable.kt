package com.skintker.data.db.logs

import org.jetbrains.exposed.dao.id.IntIdTable

object AdditionalDataTable : IntIdTable(){
    val stressLevel = integer("stressLevel")
    val weatherHumidity = integer("weatherHumidity")
    val weatherTemperature = integer("weatherTemperature")
    val traveled = bool("traveled")
    val travelCity = varchar("date",100)
    val alcoholLevel = varchar("alcoholLevel",15)
    val beerTypes = varchar("foodList", 2048)
}
