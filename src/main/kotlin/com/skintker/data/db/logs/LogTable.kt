package com.skintker.data.db.logs

import org.jetbrains.exposed.dao.id.IntIdTable

object LogTable : IntIdTable(){
    val userId = varchar("userId",30)
    val dayDate = varchar("date",10)
    val foodList = varchar("foodList", 200)
    val irritation = reference("irritation", IrritationTable)
    val additionalData = reference("additionalData", AdditionalDataTable)
}
