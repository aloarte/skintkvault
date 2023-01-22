package com.skintker.data.datasources.impl

import com.skintker.data.datasources.IrritationsDatasource
import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.db.logs.IrritationTable
import com.skintker.data.db.logs.entities.EntityParsers.irritationEntityToBo
import com.skintker.data.db.logs.entities.IrritationEntity
import com.skintker.data.dto.logs.Irritation

class IrritationsDatasourceImpl : IrritationsDatasource {

    override suspend fun deleteIrritations(idListValue: List<Int>) = dbQuery{
        idListValue.forEach { id ->
            IrritationEntity.find { IrritationTable.id eq id }.singleOrNull()?.delete()
        }
    }

    override suspend fun editIrritation(irritationId: Int, irritationValue: Irritation): IrritationEntity? = dbQuery {
        IrritationEntity.find { IrritationTable.id eq irritationId }.singleOrNull()?.let {
            it.value = irritationValue.overallValue
            it.zoneValues = irritationValue.zoneValues.joinToString(",")
            it
        }
    }

    override suspend fun getAllIrritationsWithValue(value: Int): List<Irritation> =
        dbQuery { IrritationEntity.all().sortedBy { it.value }.map(::irritationEntityToBo) }


    override suspend fun getIrritation(id: Int): Irritation? = dbQuery {
        IrritationEntity.find { IrritationTable.id eq id }.singleOrNull()?.let { irritationEntityToBo(it) }
    }

    override suspend fun addNewIrritation(irritationValue: Irritation): IrritationEntity = dbQuery {
        IrritationEntity.new {
            value = irritationValue.overallValue
            zoneValues = irritationValue.zoneValues.joinToString(",")
        }
    }

}

