package com.skintker.data

import com.skintker.data.datasources.impl.AdditionalDataDatasourceImpl
import com.skintker.data.db.DatabaseFactory
import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.AlcoholLevel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class AdditionalDataDatasourceTest {

    companion object {
        private const val adStress = 10
        private const val adWeatherHumidity = 0
        private const val adWeatherTemperature = 5
        private const val adTraveled = true
        private const val adCity = "Madrid"
        private val adAlcohol = AlcoholLevel.Some
        private val adBeerTypes = listOf("Ale")
        private val weather = AdditionalData.Weather(adWeatherHumidity, adWeatherTemperature)
        private val travel = AdditionalData.Travel(adTraveled, adCity)
        private val additionalData = AdditionalData(adStress, weather, travel, adAlcohol, adBeerTypes)
        private val additionalDataEdited = AdditionalData(
            3,
            AdditionalData.Weather(1, 2),
            AdditionalData.Travel(false, ""),
            AlcoholLevel.FewWine,
            emptyList()
        )
    }

    private val dataSource = AdditionalDataDatasourceImpl()

    @Before
    fun setup() {
        DatabaseFactory.init()
    }

    @Test
    fun `test add new additional data`() {
        val result = runBlocking { dataSource.addNewAdditionalData(additionalData) }
        assertEquals(adStress, result.stressLevel)
        assertEquals(weather.humidity, result.weatherHumidity)
        assertEquals(weather.temperature, result.weatherTemperature)
        assertEquals(travel.traveled, result.traveled)
        assertEquals(travel.city, result.travelCity)
        assertEquals(adAlcohol.name, result.alcoholLevel)
        assertEquals(adBeerTypes.joinToString(","), result.beerTypes)
    }

    @Test
    fun `test get additional data success`() {
        val result = runBlocking {
            dataSource.addNewAdditionalData(additionalData)
            dataSource.getAdditionalData(1)
        }

        assertNotNull(result)
        assertEquals(additionalData, result)
    }

    @Test
    fun `test get additional data not found`() {
        val result = runBlocking {
            dataSource.getAdditionalData(1)
        }

        assertNull(result)
    }

    @Test
    fun `test delete additional data success`() {
        val result = runBlocking {
            dataSource.addNewAdditionalData(additionalData)
            dataSource.deleteAdditionalData(listOf(1))
            dataSource.getAdditionalData(1)
        }

        assertNull(result)
    }

    @Test
    fun `test delete additional data not found`() {
        val result = runBlocking {
            dataSource.deleteAdditionalData(listOf(1))
            dataSource.getAdditionalData(1)
        }

        assertNull(result)
    }

    @Test
    fun `test edit additional data success`() {
        val result = runBlocking {
            val entityValue = dataSource.addNewAdditionalData(additionalData)
            dataSource.editAdditionalData(entityValue.id.value, additionalDataEdited)
            dataSource.getAdditionalData(1)
        }

        assertNotNull(result)
        assertEquals(additionalDataEdited.stressLevel, result.stressLevel)
    }

    @Test
    fun `test get all additional data with value empty list`() {
        val result = runBlocking {
            dataSource.getAllAdditionalDataValue(10)
        }

        assertTrue(result.isEmpty())
    }

    @Test
    fun `test get all additional data with value`() {
        val result = runBlocking {
            dataSource.addNewAdditionalData(additionalData)
            dataSource.addNewAdditionalData(additionalData)
            dataSource.getAllAdditionalDataValue(10)
        }

        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)

    }

}