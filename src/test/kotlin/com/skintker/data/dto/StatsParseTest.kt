package com.skintker.data.dto

import com.skintker.TestConstants.jsonStats
import com.skintker.TestConstants.stats
import com.skintker.data.dto.stats.StatsDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation
import org.apache.commons.math3.stat.inference.OneWayAnova
import org.junit.Test
import kotlin.test.assertEquals

class StatsParseTest {

    @Test
    fun `test parse stats json`() {
        val jsonDecoded = Json.decodeFromString<StatsDto>(jsonStats)

        assertEquals(stats,jsonDecoded)
    }




    @Test
    fun `test increase`() {

        val map = mutableMapOf(
            "Meat" to listOf(false,false,false,false),
            "Fish" to listOf(false,false,false,false)
            )


        // Datos de ejemplo
        val intensidades = listOf(
            9.0,9.0,9.0,9.0,9.0,9.0,9.0,9.0,9.0,10.0,
            1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,2.0,2.0,
            5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,4.0,4.0/*,
            3.0,2.0,1.0,3.0,2.0,3.0,2.0,5.0,5.0,6.0,
            1.0,2.0,6.0,3.0,2.0,5.0,5.0,1.0,2.0,3.0*/)
        val humedades = listOf(
            1,1,1,1,1,1,1,1,1,1,
            2,2,2,2,2,2,2,2,2,2,
            3,3,3,3,3,3,3,3,3,3/*,
            4,4,4,4,4,4,4,4,4,4,
            5,5,5,5,5,5,5,5,5,5*/)


        // Asegúrate de tener al menos dos grupos
        require(humedades.toSet().size >= 2) { "Se necesitan al menos dos niveles de humedad diferentes" }

        // Agrupa las intensidades por nivel de humedad
        val grupos = humedades.toSet().associateWith { nivel ->
            intensidades.filterIndexed { index, _ -> humedades[index] == nivel }.toDoubleArray()
        }

        val validGroups = grupos.filter { it.value.size>1 }

        // Realiza el ANOVA
        val anova = OneWayAnova()
        val fTest = anova.anovaFValue(validGroups.values)
        val pTest = anova.anovaPValue(validGroups.values)


        // Imprime el resultado
        println("Valor F del ANOVA: $fTest")

        // Puedes realizar pruebas de comparaciones múltiples si el resultado del ANOVA es significativo
        if (pTest > 0.05) {

            println("No hay diferencia significativa entre los niveles de humedad.")


        } else {

            val list = mutableMapOf<Int,Double>()
            validGroups.forEach {
                list[it.key] = it.value.average()
            }

            val maxEntry = list.entries.maxByOrNull { it.value }
            val pair = maxEntry?.toPair()
            println("Hay una diferencia significativa entre al menos dos niveles de humedad. El valor más alto es ${pair?.second}, que viene del grupo ${pair?.first}")
        }

    }

    fun MutableMap<String, List<Boolean>>.increaseValue(key: String, index: Int) {
        this[key]?.toMutableList()?.let {
            it[index] = true
            this[key] = it
        }
    }


}

