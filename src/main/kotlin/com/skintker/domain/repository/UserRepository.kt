package com.skintker.domain.repository

interface UserRepository {
    suspend fun isUserValid(userId: String): Boolean

}