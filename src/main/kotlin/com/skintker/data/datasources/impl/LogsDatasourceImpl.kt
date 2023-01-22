package com.skintker.data.datasources.impl

import com.skintker.data.datasources.AdditionalDataDatasource
import com.skintker.data.datasources.IrritationsDatasource
import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.db.logs.LogTable
import com.skintker.data.dto.logs.DailyLog
import com.skintker.domain.model.LogIdValues
import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.db.logs.entities.EntityParsers.logEntityToBo
import com.skintker.data.db.logs.entities.LogsEntity
import com.skintker.data.dto.logs.AdditionalData
import com.skintker.data.dto.logs.Irritation
import org.jetbrains.exposed.sql.*

class LogsDatasourceImpl(
    private val irritationsDatasource: IrritationsDatasource,
    private val additionalDataDatasource: AdditionalDataDatasource
) : LogsDatasource {

    override suspend fun getAllLogs(userId: String): List<DailyLog> = dbQuery {
        LogTable
            .select { LogTable.userId eq userId }
            .map {
                logEntityToBo(LogsEntity.wrapRow(it))
            }
    }

    override suspend fun getLog(idValues: LogIdValues): DailyLog? = dbQuery {
        LogTable
            .select { (LogTable.userId eq idValues.userId) and (LogTable.dayDate eq idValues.dayDate) }
            .singleOrNull()?.let {
                logEntityToBo(LogsEntity.wrapRow(it))
            }
    }

    override suspend fun addNewLog(
        idValues: LogIdValues, foodValue: List<String>, irritationValue: Irritation, additionalDataValue: AdditionalData
    ): Int = dbQuery {

        val irritationEntity = irritationsDatasource.addNewIrritation(irritationValue)
        val additionalDataEntity = additionalDataDatasource.addNewAdditionalData(additionalDataValue)

        LogsEntity.new {
            dayDate = idValues.dayDate
            userId = idValues.userId
            foodList = foodValue.joinToString(",")
            irritation = irritationEntity
            additionalData = additionalDataEntity
        }.id.value

    }

    override suspend fun editLog(
        idValues: LogIdValues,
        foodValue: List<String>,
        irritationValue: Irritation,
        additionalDataValue: AdditionalData
    ): Boolean = dbQuery {
        LogTable
            .select { (LogTable.userId eq idValues.userId) and (LogTable.dayDate eq idValues.dayDate) }
            .singleOrNull()?.let { resultRow ->
                LogsEntity.wrapRow(resultRow).apply {
                    foodList = foodValue.joinToString(",")
                    irritationsDatasource.editIrritation(irritation.id.value, irritationValue)?.let {
                        irritation = it
                    }
                    additionalDataDatasource.editAdditionalData(additionalData.id.value, additionalDataValue)?.let {
                        additionalData = it
                    }
                }

            }
        true
    }

    override suspend fun deleteLog(idValues: LogIdValues): Boolean = dbQuery {
        LogsEntity.find { (LogTable.userId eq idValues.userId) and (LogTable.dayDate eq idValues.dayDate) }
            .singleOrNull()?.delete()
        true
    }

    override suspend fun deleteAllLogs(userId: String): Boolean = dbQuery {
        LogsEntity.find { LogTable.userId eq userId }
            .forEach { it.delete() }
        true
    }

}