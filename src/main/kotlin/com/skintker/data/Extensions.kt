package com.skintker.data

import com.skintker.data.dto.logs.AlcoholLevel

fun Map<String, Int>.getMaxValue(): String? {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.first
}

fun Map<AlcoholLevel, Int>.getMaxValue(): AlcoholLevel? {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.first
}

fun Map<Int, Int>.getKeyOfMaxValue(): Int {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.second ?: 0
}

fun Map<Int, Int>.getMaxValue(): Int {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.first ?: 0
}

fun MutableMap<String, Int>.increaseValue(key: String) {
    this[key] = (this[key] ?: 0) + 1
}

fun MutableMap<Int, Int>.increaseValue(key: Int) {
    this[key] = (this[key] ?: 0) + 1
}

fun MutableMap<Boolean, Int>.increaseValue(key: Boolean) {
    this[key] = (this[key] ?: 0) + 1
}

fun MutableMap<AlcoholLevel, Int>.increaseValue(key: AlcoholLevel) {
    this[key] = (this[key] ?: 0) + 1
}

fun MutableMap<String, List<Boolean>>.increaseValue(key: String, index: Int) {
    this[key]?.toMutableList()?.let {
        it[index] = true
        this[key] = it
    }
}
