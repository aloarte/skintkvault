package com.skintker.data.dto

import com.skintker.TestConstants.jsonBodyLog
import com.skintker.TestConstants.log
import com.skintker.data.dto.logs.DailyLog
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.*
class DailyLogParseTest {

    @Test
    fun testParseDailyLogJson() {
        val json = Json.decodeFromString<DailyLog>(jsonBodyLog)

        assertEquals(log,json)
    }
}