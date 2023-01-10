package com.skintker.data.db.logs

import org.jetbrains.exposed.dao.id.IntIdTable

object Logs : IntIdTable(){
    val userId = varchar("userId",40)
    val dayDate = varchar("date",10)
    val foodList = varchar("foodList", 2048)
    val irritation = reference("irritations", Irritations)
}
