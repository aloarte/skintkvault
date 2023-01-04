package com.skintker.data.datasources.impl

import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.db.logs.Logs
import com.skintker.data.dto.DailyLog
import com.skintker.model.LogIdValues
import com.skintker.data.datasources.LogsDatasource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class LogsDatasourceImpl : LogsDatasource {

    private fun resultRowToArticle(row: ResultRow) = DailyLog(
//        id = row[Logs.id],
        date = row[Logs.dayDate],
        foodList = row[Logs.foodList].split(","),
    )


    override suspend fun getAllLogs(userId: String): List<DailyLog> = dbQuery {
        Logs.select { Logs.userId eq userId }.map(::resultRowToArticle)
    }

    override suspend fun getLog(idValues: LogIdValues): DailyLog? = dbQuery {
        Logs
            .select { Logs.userId eq idValues.userId and (Logs.dayDate eq idValues.dayDate) }
            .map(::resultRowToArticle)
            .singleOrNull()
    }

    override suspend fun addNewLog(idValues: LogIdValues, food: List<String>): DailyLog? = dbQuery {
        val insertStatement = Logs.insert {
            it[userId] = idValues.userId
            it[dayDate] = idValues.dayDate
            it[foodList] = food.joinToString(",")
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }

    override suspend fun editLog(idValues: LogIdValues, food: List<String>): Boolean = dbQuery {
        Logs.update({ Logs.userId eq idValues.userId and (Logs.dayDate eq idValues.dayDate) }) {
            it[foodList] = food.joinToString(",")
        } > 0
    }

    override suspend fun deleteLog(idValues: LogIdValues): Boolean = dbQuery {
        Logs.deleteWhere { userId eq idValues.userId and (dayDate eq idValues.dayDate) } > 0
    }

    override suspend fun deleteAllLogs(userId: String): Boolean = dbQuery {
        Logs.deleteWhere { Logs.userId eq userId } > 0
    }

}