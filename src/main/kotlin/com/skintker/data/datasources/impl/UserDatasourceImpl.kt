package com.skintker.data.datasources.impl

import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.datasources.UserDatasource
import com.skintker.data.db.logs.FirebaseUserTable
import com.skintker.data.db.logs.entities.FirebaseUserEntity
import org.jetbrains.exposed.sql.select
import java.sql.BatchUpdateException

class UserDatasourceImpl : UserDatasource {

    override suspend fun addUser(id: String): Boolean = dbQuery {
        try{
            FirebaseUserEntity.new {
                userId = id
            }.id.value > 0
        }catch (ex:BatchUpdateException){
            false
        }
    }

    override suspend fun getUser(id: String): Boolean = dbQuery {
        FirebaseUserTable.select { FirebaseUserTable.userId eq id }.firstOrNull() != null
    }

    override suspend fun deleteUser(id: String): Unit = dbQuery {
        FirebaseUserTable.select { FirebaseUserTable.userId eq id }.singleOrNull()?.let {
            FirebaseUserEntity.wrapRow(it).delete()
        }
    }
}

