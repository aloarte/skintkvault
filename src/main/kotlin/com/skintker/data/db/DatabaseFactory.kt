package com.skintker.data.db

import com.skintker.data.db.logs.AdditionalDataTable
import com.skintker.data.db.logs.IrritationTable
import com.skintker.data.db.logs.LogTable
import com.skintker.data.db.logs.FirebaseUserTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(isProduction: Boolean, config: DdbbConfig) {
        if (isProduction) {
            initPostgres(config)
        } else {
            initTestDatabase()
        }
    }

    private fun initPostgres(config: DdbbConfig) {
        val database = Database.connect(
            url = "jdbc:postgresql://${config.containerName}:${config.databasePort}/${config.databaseName}",
            driver = "org.postgresql.Driver",
            user = config.userName,
            password = config.password
        )
        transaction(database) {
            SchemaUtils.drop(LogTable, IrritationTable, AdditionalDataTable, FirebaseUserTable)
            SchemaUtils.create(LogTable, IrritationTable, AdditionalDataTable, FirebaseUserTable)
        }
    }

    private fun initTestDatabase() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"

        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            // Drop schemas always for test databases
            SchemaUtils.drop(LogTable, IrritationTable, AdditionalDataTable, FirebaseUserTable)
            SchemaUtils.create(LogTable, IrritationTable, AdditionalDataTable, FirebaseUserTable)
        }
    }


    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
