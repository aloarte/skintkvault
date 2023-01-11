package com.skintker.data.db.logs

import org.jetbrains.exposed.dao.id.IntIdTable

object AdditionalDataTable : IntIdTable(){
    val stressLevel = integer("stressLevel")
    val weatherHumidity = integer("weatherHumidity")
    val weatherTemperature = integer("weatherTemperature")
    val traveled = bool("traveled")
    val travelCity = varchar("travelCity",20)
    val alcoholLevel = varchar("alcoholLevel",10)
    val beerTypes = varchar("beerTypes", 200)
}
