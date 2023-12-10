package com.skintker.data.components

import com.skintker.data.dto.logs.AdditionalData
import com.skintker.data.dto.logs.AlcoholLevel
import com.skintker.data.dto.logs.DailyLog
import com.skintker.data.dto.logs.Irritation
import com.skintker.data.components.InputValidator.Companion.VALIDATION_ERROR_DATE
import com.skintker.data.components.InputValidator.Companion.VALIDATION_ERROR_LEVEL
import com.skintker.data.components.InputValidator.Companion.VALIDATION_ERROR_SLIDER
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InputValidatorTest {

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
