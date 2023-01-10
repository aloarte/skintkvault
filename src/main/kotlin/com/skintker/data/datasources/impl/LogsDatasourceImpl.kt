package com.skintker.data.datasources.impl

import com.skintker.data.datasources.IrritationsDatasource
import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.db.logs.Logs
import com.skintker.data.dto.DailyLog
import com.skintker.model.LogIdValues
import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.db.logs.entities.EntityParsers.logEntityToBo
import com.skintker.data.db.logs.entities.LogsEntity
import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.Irritation
import org.jetbrains.exposed.sql.*

class LogsDatasourceImpl(private val irritationsDatasource: IrritationsDatasource) : LogsDatasource {

    override suspend fun getAllLogs(userId: String): List<DailyLog> = dbQuery {
        Logs.select { Logs.userId eq userId }.map {
            logEntityToBo(LogsEntity.wrapRow(it))
        }
    }

    override suspend fun getLog(idValues: LogIdValues): DailyLog? = dbQuery {
        Logs.select { (Logs.userId eq idValues.userId) and (Logs.dayDate eq idValues.dayDate) }.singleOrNull()?.let {
            logEntityToBo(LogsEntity.wrapRow(it))
        }
    }

    override suspend fun addNewLog(
        idValues: LogIdValues, food: List<String>, irritationData: Irritation, additionalData: AdditionalData
    ): Int = dbQuery {

        val irritationEntity = irritationsDatasource.addNewIrritation(irritationData)

        LogsEntity.new {
            dayDate = idValues.dayDate
            userId = idValues.userId
            foodList = food.joinToString(",")
            irritation = irritationEntity
        }.id.value

    }

    override suspend fun editLog(
        idValues: LogIdValues,
        foodValue: List<String>,
        irritationValue: Irritation,
        additionalDataValue: AdditionalData
    ): Boolean = dbQuery {
        Logs.select { (Logs.userId eq idValues.userId) and (Logs.dayDate eq idValues.dayDate) }.singleOrNull()
            ?.let { resultRow ->
                LogsEntity.wrapRow(resultRow).apply {
                    foodList = foodValue.joinToString(",")
                    irritationsDatasource.editIrritation(irritation.id.value, irritationValue)?.let { irritation = it }
                }

            }
        true
    }

    override suspend fun deleteLog(idValues: LogIdValues): Boolean = dbQuery {
        LogsEntity.find { (Logs.userId eq idValues.userId) and (Logs.dayDate eq idValues.dayDate) }
            .singleOrNull()?.let {
                it.delete()
            }
        true
    }

    override suspend fun deleteAllLogs(userId: String): Boolean = dbQuery {
        LogsEntity.find { Logs.userId eq userId }.forEach { it.delete() }
        true
    }

}