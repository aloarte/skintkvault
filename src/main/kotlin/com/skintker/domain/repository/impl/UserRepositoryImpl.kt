package com.skintker.domain.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.skintker.data.datasources.UserDatasource
import com.skintker.domain.repository.UserRepository
import org.slf4j.LoggerFactory

class UserRepositoryImpl(private val userDatasource: UserDatasource/*, private val firebaseAuth: FirebaseAuth*/) :
    UserRepository {
    private fun getLogger() = LoggerFactory.getLogger(UserRepositoryImpl::class.java)

    override suspend fun getFirebaseUser(email: String): String {
        return ""/*firebaseAuth.getUserByEmail(email).uid*/
    }

    override suspend fun isUserValid(userId: String?): Boolean {
        return userId?.let { id ->
            if (userDatasource.getUser(id)) {
                true
            } else {
                try {
//                    firebaseAuth.getUser(id)
                    userDatasource.addUser(id) //If the FirebaseAuth didn't throw an exception, the user is valid
                    true
                } catch (ex: FirebaseAuthException) {
                    getLogger().error("FIREBASE Exception with user $id: ${ex.message}")
                    false
                }
            }
        } ?: false

    }

    override suspend fun isTokenValid(userToken: String?): Boolean {
        return try {
            userToken?.let {
//                firebaseAuth.verifyIdToken(userToken, true)
                true
            } ?: false
        } catch (ex: FirebaseAuthException) {
            getLogger().error("FIREBASE Exception with token $userToken: ${ex.message}")
            false
        }

    }
}
