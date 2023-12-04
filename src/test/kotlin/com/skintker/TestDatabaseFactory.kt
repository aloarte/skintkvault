package com.skintker

import com.skintker.data.db.logs.AdditionalDataTable
import com.skintker.data.db.logs.IrritationTable
import com.skintker.data.db.logs.LogTable
import com.skintker.data.db.logs.FirebaseUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabaseFactory {

    enum class DatabaseInitialization{
        Log, User,All
    }
    fun init(databaseInitialization: DatabaseInitialization) {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)

        val tableList = when(databaseInitialization){
            DatabaseInitialization.Log -> {
                listOf(LogTable, IrritationTable, AdditionalDataTable)
            }
            DatabaseInitialization.User -> {
                listOf(FirebaseUserTable)
            }
            DatabaseInitialization.All -> {
                listOf(LogTable, IrritationTable, AdditionalDataTable, FirebaseUserTable)
            }
        }

        transaction(database) {
            SchemaUtils.drop(*tableList.toTypedArray())
            SchemaUtils.create(*tableList.toTypedArray())
        }

    }
}
