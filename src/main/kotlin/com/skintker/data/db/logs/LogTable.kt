package com.skintker.data.db.logs

import com.skintker.data.Constants.DDBB_SIZE_DATE
import com.skintker.data.Constants.DDBB_SIZE_LIST
import com.skintker.data.Constants.DDBB_SIZE_USER_ID
import org.jetbrains.exposed.dao.id.IntIdTable

object LogTable : IntIdTable(){
    val userId = varchar("userId",DDBB_SIZE_USER_ID)
    val dayDate = varchar("date",DDBB_SIZE_DATE)
    val foodList = varchar("foodList", DDBB_SIZE_LIST)
    val irritation = reference("irritation", IrritationTable)
    val additionalData = reference("additionalData", AdditionalDataTable)
}
