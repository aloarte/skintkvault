package com.skintker.data.db.logs

import org.jetbrains.exposed.sql.*

object Logs : Table() {
    val id = integer("id").autoIncrement()
    val userId = varchar("userId",40)
    val dayDate = varchar("date",10)
    val foodList = varchar("body", 2048)

    override val primaryKey = PrimaryKey(userId, dayDate)
}