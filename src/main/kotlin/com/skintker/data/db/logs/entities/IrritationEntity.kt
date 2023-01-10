package com.skintker.data.db.logs.entities


import com.skintker.data.db.logs.Irritations
import com.skintker.data.db.logs.Logs
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class IrritationEntity(id: EntityID<Int>):IntEntity(id) {
    companion object : IntEntityClass<IrritationEntity>(Irritations)
    var value by Irritations.value
    var zoneValues by Irritations.zoneValues
    val logs by LogsEntity referrersOn Logs.irritation
}
