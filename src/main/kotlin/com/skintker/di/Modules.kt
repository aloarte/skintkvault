package com.skintker.di

import com.skintker.manager.DatabaseManager
import com.skintker.manager.impl.DatabaseManagerImpl
import org.koin.dsl.module

val database = module {
    factory<DatabaseManager> { DatabaseManagerImpl() }
}