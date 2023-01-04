package com.skintker.di

import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.datasources.impl.LogsDatasourceImpl
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.repository.impl.ReportsRepositoryImpl
import org.koin.dsl.module

val repository = module {
    factory<ReportsRepository> { ReportsRepositoryImpl(get()) }
}

val dao = module {
    factory<LogsDatasource> { LogsDatasourceImpl() }
}