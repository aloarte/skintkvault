package com.skintker.data.components

import com.skintker.data.dto.logs.DailyLog

class PaginationManager {

    fun getPageFromLogs(paramLimit: String?, paramOffset: String?, logList: List<DailyLog>): List<DailyLog> {

        return if (paramLimit != null && paramOffset != null) {
            val offset = paramOffset.toInt()
            val limit = paramLimit.toInt()

            if (offset + limit > logList.size) { //Is the last chunk of data from the sublist
                logList.subList(offset, logList.size)
            } else {  //Chunk of data of intLimit size
                logList.subList(offset, offset+limit)
            }
        } else {
            logList
        }
    }
}
