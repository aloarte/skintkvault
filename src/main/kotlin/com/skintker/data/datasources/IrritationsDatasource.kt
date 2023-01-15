package com.skintker.data.datasources

import com.skintker.data.db.logs.entities.IrritationEntity
import com.skintker.data.dto.logs.Irritation

interface IrritationsDatasource {

    suspend fun deleteIrritations(idListValue: List<Int>)

    suspend fun editIrritation(irritationId:Int,irritationValue: Irritation): IrritationEntity?

    suspend fun getAllIrritationsWithValue(value:Int): List<Irritation>

    suspend fun getIrritation(id: Int): Irritation?

    suspend fun addNewIrritation(irritationValue: Irritation): IrritationEntity

}