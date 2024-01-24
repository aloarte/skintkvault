package com.skintker.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserReturnType{
    UserExist,UserInserted, UserNotInserted, InvalidToken,FirebaseError,FirebaseDisabled
}
