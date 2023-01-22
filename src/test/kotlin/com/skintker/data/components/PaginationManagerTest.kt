package com.skintker.data.components

import com.skintker.TestConstants.bigLogList
import com.skintker.TestConstants.date
import com.skintker.TestConstants.date2
import com.skintker.TestConstants.date3
import com.skintker.TestConstants.date4
import com.skintker.TestConstants.date5
import com.skintker.TestConstants.date6
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PaginationManagerTest {

    private lateinit var paginationManager: PaginationManager

    @Before
    fun setup(){
        paginationManager = PaginationManager()
    }

    @Test
    fun `test get page from logs, good pagination start chunk`() {
        val result = paginationManager.getPageFromLogs("2","0", bigLogList)

        assertEquals(2,result.size)
        assertEquals(date,result[0].date)
        assertEquals(date2,result[1].date)
    }

    @Test
    fun `test get page from logs, good pagination middle chunk`() {
        val result = paginationManager.getPageFromLogs("2","2", bigLogList)

        assertEquals(2,result.size)
        assertEquals(date3,result[0].date)
        assertEquals(date4,result[1].date)
    }

    @Test
    fun `test get page from logs, good pagination end chunk, less items than limit`() {
        val result = paginationManager.getPageFromLogs("3","4", bigLogList)

        assertEquals(2,result.size)
        assertEquals(date5,result[0].date)
        assertEquals(date6,result[1].date)
    }

}
