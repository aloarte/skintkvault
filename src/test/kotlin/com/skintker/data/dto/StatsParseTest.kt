package com.skintker.data.dto

import com.skintker.TestConstants.jsonStats
import com.skintker.TestConstants.stats
import com.skintker.data.dto.stats.StatsDto
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class StatsParseTest {

    @Test
    fun `test parse stats json`() {
        val jsonDecoded = Json.decodeFromString<StatsDto>(jsonStats)

        assertEquals(stats,jsonDecoded)
    }
}

