package com.skintker.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class UserResult {
    @Serializable
    data object UserExist : UserResult()

    @Serializable
    data class UserInsert(val inserted: Boolean) : UserResult()

    @Serializable
    data object InvalidToken : UserResult()

    @Serializable
    data class FirebaseError(val message: String?) : UserResult()

    @Serializable
    data object FirebaseDisabled : UserResult()

}