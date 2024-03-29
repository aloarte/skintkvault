package com.skintker.domain.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.skintker.data.datasources.UserDatasource
import com.skintker.domain.model.UserReturnType
import com.skintker.domain.repository.UserRepository
import org.slf4j.LoggerFactory

class UserRepositoryImpl(private val userDatasource: UserDatasource, private val firebaseAuth: FirebaseAuth) :
    UserRepository {
    private fun getLogger() = LoggerFactory.getLogger(UserRepositoryImpl::class.java)

    override suspend fun getFirebaseUser(email: String): String {
        return try {
            firebaseAuth.getUserByEmail(email).uid
        } catch (ex: FirebaseAuthException) {
            getLogger().error("FIREBASE Exception during getFirebaseUser for mail $email: ${ex.message}")
            ""
        }
    }

    override suspend fun userExists(userId: String?): Boolean =
        userId?.let { userDatasource.userExists(userId) } ?: false

    override suspend fun removeUser(userId: String) = userDatasource.deleteUser(userId)

    override suspend fun addUser(userId: String): UserReturnType {
        return if (userDatasource.userExists(userId)) {
            UserReturnType.UserExist
        } else {
            addUserFirebase(userId)
        }
    }

    private suspend fun addUserFirebase(userId:String):UserReturnType{
        return try {
            val fbUser = firebaseAuth.getUser(userId)
            if (!fbUser.isDisabled) {
                val inserted = userDatasource.addUser(userId)
                if (inserted)UserReturnType.UserInserted else UserReturnType.UserNotInserted
            } else {
                getLogger().error("FIREBASE Error user disabled $userId")
                UserReturnType.FirebaseDisabled
            }

        } catch (ex: FirebaseAuthException) {
            getLogger().error("FIREBASE Exception with user $userId: ${ex.message}")
            UserReturnType.FirebaseError
        }
    }

    override suspend fun isTokenValid(userToken: String?): Boolean {
        return try {
            userToken?.let {
                firebaseAuth.verifyIdToken(userToken, true)
                true
            } ?: false
        } catch (ex: FirebaseAuthException) {
            getLogger().error("FIREBASE Exception with token $userToken: ${ex.message}")
            false
        }
    }
}
