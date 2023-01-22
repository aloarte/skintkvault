package com.skintker.data.components

import com.skintker.data.Constants.LEVEL_MAX
import com.skintker.data.Constants.LEVEL_MIN
import com.skintker.data.Constants.SLIDER_MAX
import com.skintker.data.Constants.SLIDER_MIN
import com.skintker.data.dto.logs.DailyLog
import com.skintker.domain.repository.UserRepository
import java.text.ParseException
import java.text.SimpleDateFormat

class InputValidator(private val userRepository: UserRepository) {

    companion object {
        const val VALIDATION_ERROR_DATE = "Invalid log data, the date is invalid. Follow the pattern mm-dd-yyyy"
        const val VALIDATION_ERROR_LEVEL = "Invalid log data, the level values must be between 1-10"
        const val VALIDATION_ERROR_SLIDER = "Invalid log data, the weather values must be between 1-5"
    }

    suspend fun isUserIdInvalid(userId: String?): Boolean {
        return if (userId.isNullOrEmpty()) {
            return true
        } else {
            userRepository.isUserValid(userId).not()
        }
    }

    fun isLogInvalid(log: DailyLog) = when {
        isDateValid(log.date).not() -> VALIDATION_ERROR_DATE
        isNumLevelValid(log.irritation.overallValue).not() -> VALIDATION_ERROR_LEVEL
        isNumLevelValid(log.additionalData.stressLevel).not() -> VALIDATION_ERROR_LEVEL
        isNumSliderValid(log.additionalData.weather.temperature).not() -> VALIDATION_ERROR_SLIDER
        isNumSliderValid(log.additionalData.weather.humidity).not() -> VALIDATION_ERROR_SLIDER
        else -> null
    }


    private fun isDateValid(date: String): Boolean {
        if (!date.matches("[0-3]\\d-[01]\\d-\\d{4}".toRegex())) return false
        val dateFormat = SimpleDateFormat("dd-MM-YYYY").apply { isLenient = false }
        return try {
            dateFormat.parse(date)
            true
        } catch (ex: ParseException) {
            false
        }
    }

    //A level must be between 1 an 10
    private fun isNumLevelValid(numLevel: Int) = numLevel in LEVEL_MIN..LEVEL_MAX

    //A slider must be between 1 an 5
    private fun isNumSliderValid(numLevel: Int) = numLevel in SLIDER_MIN..SLIDER_MAX

    fun arePaginationIndexesInvalid(limit: String?, offset: String?, listSize: Int): Boolean {
        return try {
            val parsedOffset = offset?.toInt()
            val parsedLimit = limit?.toInt()

            //Verify that are whole numbers
            if (isOffsetValid(parsedOffset) && isLimitValid(parsedLimit)) {
                val maxPaginatedIndex = parsedOffset!! + parsedLimit!!
                if (maxPaginatedIndex < listSize) {
                    false
                } else {
                    parsedOffset > listSize
                }
            } else {
                true
            }
        } catch (ex: NumberFormatException) {
            true
        }
    }

    private fun isOffsetValid(index: Int?) = index != null && index >= 0

    private fun isLimitValid(index: Int?) = index != null && index > 0

}
