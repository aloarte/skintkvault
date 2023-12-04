package com.skintker.data.db

data class DdbbConfig(
    val userName:String,
    val password:String,
    val databaseName:String,
    val databasePort:String,
    val containerName:String
)