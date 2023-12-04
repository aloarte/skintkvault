package com.skintker.data.db.logs.entities

import com.skintker.data.db.logs.FirebaseUserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>):IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(FirebaseUserTable)
    var userId by FirebaseUserTable.userId
}
