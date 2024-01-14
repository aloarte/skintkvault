package com.skintker.data.datasources.impl

import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.datasources.UserDatasource
import com.skintker.data.db.logs.FirebaseUserTable
import com.skintker.data.db.logs.entities.FirebaseUserEntity
import com.skintker.domain.repository.impl.UserRepositoryImpl
import org.jetbrains.exposed.sql.select
import org.slf4j.LoggerFactory
import java.sql.BatchUpdateException

class UserDatasourceImpl : UserDatasource {

    private fun getLogger() = LoggerFactory.getLogger(UserDatasource::class.java)

    override suspend fun addUser(id: String): Boolean = dbQuery {
        try{
            FirebaseUserEntity.new {
                userId = id
            }.id.value > 0
        }catch (ex:BatchUpdateException){
            getLogger().error("Exception during addUser for id $id: ${ex.message}")
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

