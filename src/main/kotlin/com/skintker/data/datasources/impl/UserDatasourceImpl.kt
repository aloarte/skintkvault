package com.skintker.data.datasources.impl

import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.datasources.UserDatasource
import com.skintker.data.db.logs.FirebaseUserTable
import com.skintker.data.db.logs.entities.UserEntity
import org.jetbrains.exposed.sql.select

class UserDatasourceImpl : UserDatasource {

    override suspend fun addUser(id: String): Boolean = dbQuery {
       UserEntity.new {
           userId = id
       }.id.value > 0
    }

    override suspend fun getUser(id: String): Boolean = dbQuery {
        FirebaseUserTable.select{ FirebaseUserTable.userId eq id }.singleOrNull()!=null
    }

    override suspend fun deleteUser(id: String): Unit = dbQuery {
        FirebaseUserTable.select{ FirebaseUserTable.userId eq id }.singleOrNull()?.let{
            UserEntity.wrapRow(it).delete()
        }
    }

}

