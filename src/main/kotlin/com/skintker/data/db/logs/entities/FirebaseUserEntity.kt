package com.skintker.data.db.logs.entities

import com.skintker.data.db.logs.FirebaseUserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FirebaseUserEntity(id: EntityID<Int>):IntEntity(id) {
    companion object : IntEntityClass<FirebaseUserEntity>(FirebaseUserTable)
    var userId by FirebaseUserTable.userId
}
