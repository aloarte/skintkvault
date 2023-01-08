package com.skintker.di

import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.datasources.impl.LogsDatasourceImpl
import com.skintker.data.repository.ReportsRepository
import com.skintker.data.repository.impl.ReportsRepositoryImpl
import com.skintker.data.validators.UserInfoValidator
import org.koin.dsl.module

val validators = module {
    factory { UserInfoValidator () }
}

val repository = module {
    factory<ReportsRepository> { ReportsRepositoryImpl(get()) }
}

val dao = module {
    factory<LogsDatasource> { LogsDatasourceImpl() }
}