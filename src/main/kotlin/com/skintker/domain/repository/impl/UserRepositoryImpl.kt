package com.skintker.domain.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.skintker.data.datasources.UserDatasource
import com.skintker.domain.repository.UserRepository
import org.slf4j.LoggerFactory

class UserRepositoryImpl(private val userDatasource: UserDatasource, private val firebaseAuth: FirebaseAuth) :
    UserRepository {
    private fun getLogger() = LoggerFactory.getLogger(UserRepositoryImpl::class.java)

    override suspend fun isUserValid(userId: String): Boolean {
        return if(userDatasource.getUser(userId)){
            true
        }
        else{
            try{
                firebaseAuth.getUser(userId)
                userDatasource.addUser(userId) //If the FirebaseAuth didn't throw an exception, the user is valid
                true
            }
            catch (ex: FirebaseAuthException){
                getLogger().error("Exception with user $userId: ${ex.message}")
                false
            }
        }
    }
}
