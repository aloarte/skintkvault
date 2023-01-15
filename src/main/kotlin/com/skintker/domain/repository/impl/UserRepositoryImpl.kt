package com.skintker.domain.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.skintker.data.datasources.UserDatasource
import com.skintker.domain.repository.UserRepository

class UserRepositoryImpl(private val userDatasource: UserDatasource, private val firebaseAuth: FirebaseAuth) :
    UserRepository {
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
                false
            }
        }
    }
}