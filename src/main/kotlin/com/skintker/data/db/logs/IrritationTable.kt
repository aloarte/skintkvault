package com.skintker.data.db.logs

import org.jetbrains.exposed.dao.id.IntIdTable

object IrritationTable : IntIdTable() {
    val value = integer("value")
    val zoneValues = varchar("zoneValues", 200)
}
