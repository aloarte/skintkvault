package com.skintker.data.components

import com.skintker.data.dto.logs.AdditionalData
import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.logs.Irritation
import com.skintker.data.components.InputValidator.Companion.VALIDATION_ERROR_DATE
import com.skintker.data.components.InputValidator.Companion.VALIDATION_ERROR_LEVEL
import com.skintker.data.components.InputValidator.Companion.VALIDATION_ERROR_SLIDER
import io.mockk.InternalPlatformDsl.toArray
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue



class InputValidatorTest {
    @Test
    fun mainaa() {
        // Ejemplo de datos
        val nivelDePicor = listOf(10, 4, 1, 8, 9, 6, 7, 3, 9, 10)
        val haberComidoPescado = listOf(true, false, false, true, true, false, true, false, true, true)
        val haberComidoArrpz = listOf(false, true, true, false, false, true, false, true, false, true)

        val haberBebidoAlcohol = listOf(false, true, false, true, false, true, false, true, true, false)

        // Calcular la correlación de punto biserial entre "nivel de picor" y "haber comido pescado"
        val correlationComidoPescado = calculatePointBiserialCorrelation(nivelDePicor, haberComidoPescado)
        println("Correlación de punto biserial con haber comido pescado: $correlationComidoPescado")

        // Calcular la correlación de punto biserial entre "nivel de picor" y "haber bebido alcohol"
        val correlationBebidoArroz = calculatePointBiserialCorrelation(nivelDePicor, haberComidoArrpz)
        println("Correlación de punto biserial con haber bebido arroz: $correlationBebidoArroz")

        // Calcular la correlación de punto biserial entre "nivel de picor" y "haber bebido alcohol"
        val correlationBebidoAlcohol = calculatePointBiserialCorrelation(nivelDePicor, haberBebidoAlcohol)
        println("Correlación de punto biserial con haber bebido alcohol: $correlationBebidoAlcohol")
    }

    fun calculatePointBiserialCorrelation(continuousData: List<Int>, binaryData: List<Boolean>): Double {
        val pearsonsCorrelation = PearsonsCorrelation()
        return pearsonsCorrelation.correlation(continuousData.map { it.toDouble() }.toDoubleArray(), binaryData.map { if (it) 1.0 else 0.0 }.toDoubleArray())
    }

    private lateinit var validator: InputValidator

    @Before
    fun setup(){
        validator = InputValidator()
    }

    @Test
    fun `test is log valid success`() {
        val log = DailyLog(
            "31-12-2023",
            emptyList(),
            Irritation(10, emptyList()),
            AdditionalData(
                1,
                AdditionalData.Weather(1,5),
                AdditionalData.Travel(true,""),
                AlcoholLevel.None,
                emptyList()
            )
        )

        val result = validator.isLogInvalid(log)

        assertNull(result)
    }

    @Test
    fun `test is log valid date error`() {
        val log = DailyLog(
            "24-2023",
            emptyList(),
            Irritation(10, emptyList()),
            AdditionalData(
                1,
                AdditionalData.Weather(1,5),
                AdditionalData.Travel(true,""),
                AlcoholLevel.None,
                emptyList()
            )
        )

        val result = validator.isLogInvalid(log)

        assertEquals(VALIDATION_ERROR_DATE,result)
    }

    @Test
    fun `test is log valid level overallValue error`() {
        val log = DailyLog(
            "31-12-2023",
            emptyList(),
            Irritation(12, emptyList()),
            AdditionalData(
                1,
                AdditionalData.Weather(1,5),
                AdditionalData.Travel(true,""),
                AlcoholLevel.None,
                emptyList()
            )
        )

        val result = validator.isLogInvalid(log)

        assertEquals(VALIDATION_ERROR_LEVEL,result)
    }

    @Test
    fun `test is log valid level stressLevel error`() {
        val log = DailyLog(
            "31-12-2023",
            emptyList(),
            Irritation(10, emptyList()),
            AdditionalData(
                0,
                AdditionalData.Weather(1,5),
                AdditionalData.Travel(true,""),
                AlcoholLevel.None,
                emptyList()
            )
        )

        val result = validator.isLogInvalid(log)

        assertEquals(VALIDATION_ERROR_LEVEL,result)
    }

    @Test
    fun `test is log valid range humidity error`() {
        val log = DailyLog(
            "31-12-2023",
            emptyList(),
            Irritation(10, emptyList()),
            AdditionalData(
                1,
                AdditionalData.Weather(0,5),
                AdditionalData.Travel(true,""),
                AlcoholLevel.None,
                emptyList()
            )
        )

        val result = validator.isLogInvalid(log)

        assertEquals(VALIDATION_ERROR_SLIDER,result)
    }

    @Test
    fun `test is log valid range temperature error`() {
        val log = DailyLog(
            "31-12-2023",
            emptyList(),
            Irritation(10, emptyList()),
            AdditionalData(
                1,
                AdditionalData.Weather(1,6),
                AdditionalData.Travel(true,""),
                AlcoholLevel.None,
                emptyList()
            )
        )

        val result = validator.isLogInvalid(log)

        assertEquals(VALIDATION_ERROR_SLIDER,result)
    }

    @Test
    fun `test are pagination index valid, invalid inputs`(){
        assertTrue(validator.arePaginationIndexesInvalid("","",20))
        assertTrue(validator.arePaginationIndexesInvalid("-1","",20))
        assertTrue(validator.arePaginationIndexesInvalid("","-1",20))
        assertTrue(validator.arePaginationIndexesInvalid("Text","",20))
        assertTrue(validator.arePaginationIndexesInvalid("","Text",20))
        assertTrue(validator.arePaginationIndexesInvalid("0","0",20))
        assertTrue(validator.arePaginationIndexesInvalid("100","25",23))

    }
    @Test
    fun `test are pagination index valid, valid inputs`(){
        assertFalse(validator.arePaginationIndexesInvalid("20","0",23))
        assertFalse(validator.arePaginationIndexesInvalid("20","20",23))
        assertFalse(validator.arePaginationIndexesInvalid("100","0",23))

    }

}