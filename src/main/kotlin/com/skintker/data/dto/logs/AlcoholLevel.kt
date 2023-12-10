package com.skintker.data.dto.logs

enum class AlcoholLevel(val value:Int) {
    None(0), Beer(1), Wine(2), Distilled(3), Mixed(4);

    companion object{
        fun fromValue(value: Int): AlcoholLevel {
            return entries.firstOrNull { it.value == value } ?: None
        }
    }
}
