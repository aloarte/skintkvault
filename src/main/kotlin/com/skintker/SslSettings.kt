package com.skintker

import java.io.FileInputStream
import java.security.KeyStore

object SslSettings {
    fun getKeyStore(ksPath:String, ksPassword:String): KeyStore {
        val keyStoreFile = FileInputStream(ksPath)
        val keyStorePassword = ksPassword.toCharArray()
        val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(keyStoreFile, keyStorePassword)
        return keyStore
    }

}