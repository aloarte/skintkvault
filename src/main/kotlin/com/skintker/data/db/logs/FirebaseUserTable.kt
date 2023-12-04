package com.skintker.data.db.logs

import com.skintker.data.Constants.DDBB_SIZE_USER_ID
import org.jetbrains.exposed.dao.id.IntIdTable

object FirebaseUserTable : IntIdTable(){
    val userId = varchar("userId", DDBB_SIZE_USER_ID)
}
