package com.skintker.di

import com.google.firebase.auth.FirebaseAuth
import com.skintker.data.datasources.*
import com.skintker.data.datasources.impl.*
import com.skintker.data.components.StatsDataProcessor
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.repository.impl.ReportsRepositoryImpl
import com.skintker.data.components.InputValidator
import com.skintker.data.components.PaginationManager
import com.skintker.domain.repository.StatsRepository
import com.skintker.domain.repository.UserRepository
import com.skintker.domain.repository.impl.StatsRepositoryImpl
import com.skintker.domain.repository.impl.UserRepositoryImpl
import org.koin.dsl.module

val components = module {
    factory { InputValidator(get()) }
    factory { PaginationManager() }
    factory { StatsDataProcessor() }
}

val repository = module {
    factory<StatsRepository> { StatsRepositoryImpl(get(),get()) }
    factory<ReportsRepository> { ReportsRepositoryImpl(get()) }
    factory<UserRepository> { UserRepositoryImpl(get(),get()) }

}

val dao = module {
    factory<StatsDatasource> { StatsDatasourceImpl(get()) }
    factory<LogsDatasource> { LogsDatasourceImpl(get(),get()) }
    factory<IrritationsDatasource> { IrritationsDatasourceImpl() }
    factory<AdditionalDataDatasource> { AdditionalDataDatasourceImpl() }
    factory<UserDatasource> { UserDatasourceImpl() }
}

val single = module {
    single { FirebaseAuth.getInstance() }
}