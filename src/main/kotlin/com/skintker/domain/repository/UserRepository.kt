package com.skintker.domain.repository

interface UserRepository {

    suspend fun getFirebaseUser(email: String): String

    suspend fun isUserValid(userId: String?): Boolean

    suspend fun isTokenValid(userToken: String?): Boolean
}
