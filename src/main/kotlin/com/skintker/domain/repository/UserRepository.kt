package com.skintker.domain.repository

import com.skintker.domain.model.UserReturnType

interface UserRepository {

    suspend fun getFirebaseUser(email: String): String

    suspend fun userExists(userId: String?): Boolean

    suspend fun removeUser(userId: String)

    suspend fun addUser(userId: String): UserReturnType

    suspend fun isTokenValid(userToken: String?): Boolean
}
