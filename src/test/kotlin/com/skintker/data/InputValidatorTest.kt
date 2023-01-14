package com.skintker.data

import com.skintker.TestConstants.userId
import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.AlcoholLevel
import com.skintker.data.dto.DailyLog
import com.skintker.data.dto.Irritation
import com.skintker.data.validators.InputValidator
import com.skintker.data.validators.InputValidator.Companion.VALIDATION_ERROR_DATE
import com.skintker.data.validators.InputValidator.Companion.VALIDATION_ERROR_LEVEL
import com.skintker.data.validators.InputValidator.Companion.VALIDATION_ERROR_SLIDER
import com.skintker.domain.repository.UserRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InputValidatorTest {

    private val userRepository = mockk<UserRepository>()

    private lateinit var validator:InputValidator

    @Before
    fun setup(){
        validator = InputValidator(userRepository)
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
    fun `test is userId invalid empty user`() {
        val result = runBlocking { validator.isUserIdInvalid("") }

        assertTrue(result)
    }

    @Test
    fun `test is userId invalid user`() {
        coEvery {  userRepository.isUserValid(userId)} returns false

        val result = runBlocking { validator.isUserIdInvalid(userId) }

        coVerify { userRepository.isUserValid(userId) }
        assertTrue(result)
    }

    @Test
    fun `test is userId valid`() {
        coEvery {  userRepository.isUserValid(userId)} returns true

        val result = runBlocking { validator.isUserIdInvalid(userId) }

        coVerify { userRepository.isUserValid(userId) }
        assertFalse(result)
    }

}