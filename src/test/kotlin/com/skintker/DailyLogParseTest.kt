package com.skintker

import com.skintker.model.DailyLog
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.*
class DailyLogParseTest {

    companion object {
        const val jsonBody = "{\"date\":\"2012-04-23T18:25:43.511Z\",\"irritation\":{\"overallValue\":9,\"zoneValues\":[\"a\",\"b\",\"c\"]},\"additionalData\":{\"stressLevel\":9,\"weather\":{\"humidity\":9,\"temperature\":9},\"travel\":{\"traveled\":true,\"city\":\"Madrid\"},\"alcoholLevel\":\"None\",\"beerTypes\":[\"ba\",\"bb\",\"bc\"]}}"
    }

    @Test
    fun testParseDailyLogJson() {
        val json = Json.decodeFromString<DailyLog>(jsonBody)
        assertEquals(jsonBody,Json.encodeToString(json))
    }
}