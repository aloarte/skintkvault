package com.skintker.data.datasources

import com.skintker.data.db.logs.entities.AdditionalDataEntity
import com.skintker.data.dto.AdditionalData


interface AdditionalDataDatasource {

    suspend fun deleteAdditionalData(idListValue: List<Int>)

    suspend fun editAdditionalData(additionalDataId:Int,additionalDataValue:AdditionalData): AdditionalDataEntity?

    suspend fun getAllAdditionalDataValue(value:Int): List<AdditionalData>

    suspend fun getAdditionalData(id: Int): AdditionalData?

    suspend fun addNewAdditionalData(additionalDataValue: AdditionalData): AdditionalDataEntity

}