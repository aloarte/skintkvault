package com.skintker.data.datasources.impl

import com.skintker.data.datasources.AdditionalDataDatasource
import com.skintker.data.db.DatabaseFactory.dbQuery
import com.skintker.data.db.logs.AdditionalDataTable
import com.skintker.data.db.logs.entities.AdditionalDataEntity
import com.skintker.data.db.logs.entities.EntityParsers.additionalDataEntityToBo
import com.skintker.data.dto.logs.AdditionalData

class AdditionalDataDatasourceImpl : AdditionalDataDatasource {

    override suspend fun deleteAdditionalData(idListValue: List<Int>) = dbQuery {
        idListValue.forEach { id ->
            AdditionalDataEntity.find { AdditionalDataTable.id eq id }.singleOrNull()?.delete()
        }
    }

    override suspend fun editAdditionalData(
        additionalDataId: Int,
        additionalDataValue: AdditionalData
    ): AdditionalDataEntity? = dbQuery {
        AdditionalDataEntity.find { AdditionalDataTable.id eq additionalDataId }.singleOrNull()?.let {
            it.stressLevel = additionalDataValue.stressLevel
            it.weatherHumidity = additionalDataValue.weather.humidity
            it.weatherTemperature = additionalDataValue.weather.temperature
            it.traveled = additionalDataValue.travel.traveled
            it.travelCity = additionalDataValue.travel.city
            it.alcoholLevel = additionalDataValue.alcoholLevel.name
            it.beerTypes = additionalDataValue.beerTypes.joinToString(",")
            it
        }
    }

    override suspend fun getAllAdditionalDataValue(value: Int): List<AdditionalData> =
        dbQuery { AdditionalDataEntity.all().sortedBy { it.stressLevel }.map(::additionalDataEntityToBo) }

    override suspend fun getAdditionalData(id: Int): AdditionalData? = dbQuery {
        AdditionalDataEntity.find { AdditionalDataTable.id eq id }.singleOrNull()?.let { additionalDataEntityToBo(it) }
    }

    override suspend fun addNewAdditionalData(additionalDataValue: AdditionalData): AdditionalDataEntity = dbQuery {
        AdditionalDataEntity.new {
            stressLevel = additionalDataValue.stressLevel
            weatherHumidity = additionalDataValue.weather.humidity
            weatherTemperature = additionalDataValue.weather.temperature
            traveled = additionalDataValue.travel.traveled
            travelCity = additionalDataValue.travel.city
            alcoholLevel = additionalDataValue.alcoholLevel.name
            beerTypes = additionalDataValue.beerTypes.joinToString(",")
        }
    }

}