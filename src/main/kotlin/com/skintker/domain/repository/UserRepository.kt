package com.skintker.domain.repository

interface UserRepository {

    suspend fun getFirebaseUser(email: String): String

    suspend fun userExists(userId: String): Boolean

    suspend fun removeUser(userId: String)

    suspend fun isUserValid(userId: String?): Boolean

    suspend fun isTokenValid(userToken: String?): Boolean
}
