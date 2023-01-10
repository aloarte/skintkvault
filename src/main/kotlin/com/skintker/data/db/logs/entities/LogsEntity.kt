package com.skintker.data.db.logs.entities

import com.skintker.data.db.logs.LogTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LogsEntity(id: EntityID<Int>):IntEntity(id) {
    companion object : IntEntityClass<LogsEntity>(LogTable)
    var userId by LogTable.userId
    var dayDate by LogTable.dayDate
    var foodList by LogTable.foodList
    var irritation by IrritationEntity referencedOn LogTable.irritation
    var additionalData by AdditionalDataEntity referencedOn LogTable.additionalData
}