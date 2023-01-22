package com.skintker.data.db.logs

import com.skintker.data.Constants.DDBB_SIZE_LIST
import org.jetbrains.exposed.dao.id.IntIdTable

object IrritationTable : IntIdTable() {
    val value = integer("value")
    val zoneValues = varchar("zoneValues", DDBB_SIZE_LIST)
}
