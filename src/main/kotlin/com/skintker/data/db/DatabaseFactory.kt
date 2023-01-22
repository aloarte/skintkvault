package com.skintker.data.db

import com.skintker.data.db.logs.AdditionalDataTable
import com.skintker.data.db.logs.IrritationTable
import com.skintker.data.db.logs.LogTable
import com.skintker.data.db.logs.UserTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(dropSchemas:Boolean) {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            if(dropSchemas) SchemaUtils.drop(LogTable,IrritationTable,AdditionalDataTable,UserTable)
            SchemaUtils.create(LogTable,IrritationTable,AdditionalDataTable,UserTable)
        }

    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
