package com.skintker.data.datasources.impl

import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.db.logs.Logs
import com.skintker.data.dto.DailyLog
import com.skintker.model.LogIdValues
import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.Irritation
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

    override suspend fun addNewLog(
        idValues: LogIdValues,
        food: List<String>?,
        irritation: Irritation?,
        additionalData: AdditionalData?
    ): DailyLog? = dbQuery {
        val insertStatement = Logs.insert { insertStatement ->
            insertStatement[userId] = idValues.userId
            insertStatement[dayDate] = idValues.dayDate
            food?.let { insertStatement[foodList] = it.joinToString(",") }

        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }

    override suspend fun editLog(
        idValues: LogIdValues,
        food: List<String>?,
        irritation: Irritation?,
        additionalData: AdditionalData?
    ): Boolean = dbQuery {
        Logs.update({ Logs.userId eq idValues.userId and (Logs.dayDate eq idValues.dayDate) }) { insertStatement ->
            food?.let { insertStatement[foodList] = it.joinToString(",") }
            irritation?.let {
                //TODO: Perform irritation insertion
            }
            additionalData?.let {
                //TODO: Perform additionalData insertion

            }
        } > 0
    }

    override suspend fun deleteLog(idValues: LogIdValues): Boolean = dbQuery {
        Logs.deleteWhere { userId eq idValues.userId and (dayDate eq idValues.dayDate) } > 0
    }

    override suspend fun deleteAllLogs(userId: String): Boolean = dbQuery {
        Logs.deleteWhere { Logs.userId eq userId } > 0
    }

}