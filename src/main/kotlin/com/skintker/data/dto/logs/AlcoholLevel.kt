package com.skintker.data.dto.logs

import com.skintker.data.Constants.ALCOHOL_LEVEL_BEER
import com.skintker.data.Constants.ALCOHOL_LEVEL_DISTILLED
import com.skintker.data.Constants.ALCOHOL_LEVEL_MIXED
import com.skintker.data.Constants.ALCOHOL_LEVEL_NONE
import com.skintker.data.Constants.ALCOHOL_LEVEL_WINE

enum class AlcoholLevel(val value: Int) {
    None(ALCOHOL_LEVEL_NONE),
    Beer(ALCOHOL_LEVEL_BEER),
    Wine(ALCOHOL_LEVEL_WINE),
    Distilled(ALCOHOL_LEVEL_DISTILLED),
    Mixed(ALCOHOL_LEVEL_MIXED);

    companion object {
        fun fromValue(value: Int): AlcoholLevel {
            return entries.firstOrNull { it.value == value } ?: None
        }
    }
}
