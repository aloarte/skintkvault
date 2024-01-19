package com.skintker.data.datasources

interface UserDatasource {

    suspend fun addUser(id: String): Boolean

    suspend fun userExists(id: String): Boolean

    suspend fun deleteUser(id: String)

}
