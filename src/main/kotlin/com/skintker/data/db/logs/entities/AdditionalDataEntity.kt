package com.skintker.data.db.logs.entities


import com.skintker.data.db.logs.AdditionalDataTable
import com.skintker.data.db.logs.IrritationTable
import com.skintker.data.db.logs.LogTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AdditionalDataEntity(id: EntityID<Int>):IntEntity(id) {
    companion object : IntEntityClass<AdditionalDataEntity>(AdditionalDataTable)
    var stressLevel by AdditionalDataTable.stressLevel
    var weatherHumidity by AdditionalDataTable.weatherHumidity
    var weatherTemperature by AdditionalDataTable.weatherTemperature
    var traveled by AdditionalDataTable.traveled
    var travelCity by AdditionalDataTable.travelCity
    var alcoholLevel by AdditionalDataTable.alcoholLevel
    var beerTypes by AdditionalDataTable.beerTypes

}
