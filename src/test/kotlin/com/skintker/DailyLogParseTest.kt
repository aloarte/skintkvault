package com.skintker

import com.skintker.data.dto.DailyLog
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.*
class DailyLogParseTest {

    companion object {
        const val jsonBody = "{\"date\":\"2012-05-41\",\"foodList\":[\"food1\",\"food2\"],\"irritation\":{\"overallValue\":7,\"zoneValues\":[\"wrist\"]},\"additionalData\":{\"stressLevel\":10,\"weather\":{\"humidity\":7,\"temperature\":1},\"travel\":{\"traveled\":false,\"city\":\"Madrid\"},\"alcoholLevel\":\"FewWine\",\"beerTypes\":[\"Ale\"]}}"
    }

    @Test
    fun testParseDailyLogJson() {
        val json = Json.decodeFromString<DailyLog>(jsonBody)

        assertEquals(jsonBody,Json.encodeToString(json))
    }
}