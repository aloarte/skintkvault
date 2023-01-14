package com.skintker.data.db.logs

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable(){
    val userId = varchar("userId",30)
}
