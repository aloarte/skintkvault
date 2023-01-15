package com.skintker.data.validators

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
        return if(userId.isNullOrEmpty()){
            return true
        }
        else{
            userRepository.isUserValid(userId).not()
        }
    }

    fun isLogInvalid(log: DailyLog): String? {
        if (!isDateValid(log.date)) {
            return VALIDATION_ERROR_DATE
        }
        if (!isNumLevelValid(log.irritation.overallValue) || !isNumLevelValid(log.additionalData.stressLevel)) {
            return VALIDATION_ERROR_LEVEL
        }
        if (!isNumSliderValid(log.additionalData.weather.temperature) || !isNumSliderValid(log.additionalData.weather.humidity)) {
            return VALIDATION_ERROR_SLIDER
        }
        return null
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
    private fun isNumLevelValid(numLevel: Int) = numLevel in 1..10

    //A slider must be between 1 an 5
    private fun isNumSliderValid(numLevel: Int) = numLevel in 1..5

}