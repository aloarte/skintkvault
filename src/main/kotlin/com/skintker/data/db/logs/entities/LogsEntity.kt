package com.skintker.data.db.logs.entities

import com.skintker.data.db.logs.Logs
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LogsEntity(id: EntityID<Int>):IntEntity(id) {
    companion object : IntEntityClass<LogsEntity>(Logs)
    var userId by Logs.userId
    var dayDate by Logs.dayDate
    var foodList by Logs.foodList
    var irritation by IrritationEntity referencedOn Logs.irritation
}