package com.skintker

import com.skintker.data.dto.stats.StatsDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.*
class StatsParseTest {

    companion object {
        const val jsonBody = "{\"enoughData\":true,\"dietaryCauses\":[\"food1\",\"food2\"],\"mostAffectedZones\":[\"wrist\"],\"alcohol\":{\"isPossible\":true,\"beerType\":\"Ale\"},\"stress\":{\"isPossible\":true,\"level\":5},\"travel\":{\"isPossible\":true,\"city\":\"city\"},\"weather\":{\"temperature\":{\"isPossible\":true,\"level\":2},\"humidity\":{\"isPossible\":true,\"level\":0}}}"
    }

    @Test
    fun `test parse stats json`() {
        val json = Json.decodeFromString<StatsDto>(jsonBody)

        assertEquals(jsonBody,Json.encodeToString(json))
    }
}

