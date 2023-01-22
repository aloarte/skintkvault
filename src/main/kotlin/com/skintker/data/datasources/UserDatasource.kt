package com.skintker.data.datasources

interface UserDatasource {

    suspend fun addUser(id: String): Boolean

    suspend fun getUser(id: String): Boolean

    suspend fun deleteUser(id: String)

}
