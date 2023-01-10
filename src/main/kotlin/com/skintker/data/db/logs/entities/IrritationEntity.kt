package com.skintker.data.db.logs.entities


import com.skintker.data.db.logs.IrritationTable
import com.skintker.data.db.logs.LogTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class IrritationEntity(id: EntityID<Int>):IntEntity(id) {
    companion object : IntEntityClass<IrritationEntity>(IrritationTable)
    var value by IrritationTable.value
    var zoneValues by IrritationTable.zoneValues
    val logs by LogsEntity referrersOn LogTable.irritation
}
