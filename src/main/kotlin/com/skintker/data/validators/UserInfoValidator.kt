package com.skintker.data.validators

class UserInfoValidator {

    fun isUserIdInvalid(userId:String?):Boolean{
        return userId.isNullOrEmpty()
    }
}