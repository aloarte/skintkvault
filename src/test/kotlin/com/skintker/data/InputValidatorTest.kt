package com.skintker.data

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.skintker.data.dto.AdditionalData
import com.skintker.data.dto.AlcoholLevel
import com.skintker.data.dto.DailyLog
import com.skintker.data.dto.Irritation
import com.skintker.data.validators.InputValidator
import com.skintker.data.validators.InputValidator.Companion.VALIDATION_ERROR_DATE
import com.skintker.data.validators.InputValidator.Companion.VALIDATION_ERROR_LEVEL
import com.skintker.data.validators.InputValidator.Companion.VALIDATION_ERROR_SLIDER
import io.mockk.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InputValidatorTest {


    private val SUT = InputValidator()

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

        val result = SUT.isLogInvalid(log)

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

        val result = SUT.isLogInvalid(log)

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

        val result = SUT.isLogInvalid(log)

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

        val result = SUT.isLogInvalid(log)

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

        val result = SUT.isLogInvalid(log)

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

        val result = SUT.isLogInvalid(log)

        assertEquals(VALIDATION_ERROR_SLIDER,result)
    }

    @Test
    fun `test is userId invalid `() {
        assertTrue(SUT.isUserIdInvalid(""))
    }

    @Test
    fun `test is userId valid`() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        every { FirebaseAuth.getInstance().getUser(any()) } returns null

        assertFalse(SUT.isUserIdInvalid("ValidFirebaseUser"))
    }

    @Test
    fun `test is userId doesn't exist in firebase `() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        every { FirebaseAuth.getInstance().getUser(any()) } throws FirebaseAuthException(FirebaseException(ErrorCode.UNAUTHENTICATED,"Exception",Exception()))

        assertTrue(SUT.isUserIdInvalid("InvalidFirebaseUser"))
    }
}