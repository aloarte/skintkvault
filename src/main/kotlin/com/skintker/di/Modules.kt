package com.skintker.di

import com.google.firebase.auth.FirebaseAuth

import com.skintker.data.components.StatsDataProcessor
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.repository.impl.ReportsRepositoryImpl
import com.skintker.data.components.InputValidator
import com.skintker.data.components.PaginationManager
import com.skintker.data.components.StatisticsCalculator
import com.skintker.data.components.StatsDataInitializer
import com.skintker.data.datasources.AdditionalDataDatasource
import com.skintker.data.datasources.IrritationsDatasource
import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.datasources.StatsDatasource
import com.skintker.data.datasources.UserDatasource
import com.skintker.data.datasources.impl.AdditionalDataDatasourceImpl
import com.skintker.data.datasources.impl.IrritationsDatasourceImpl
import com.skintker.data.datasources.impl.LogsDatasourceImpl
import com.skintker.data.datasources.impl.StatsDatasourceImpl
import com.skintker.data.datasources.impl.UserDatasourceImpl
import com.skintker.domain.repository.StatsRepository
import com.skintker.domain.repository.UserRepository
import com.skintker.domain.repository.impl.StatsRepositoryImpl
import com.skintker.domain.repository.impl.UserRepositoryImpl
import com.skintker.domain.UserManager
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import org.apache.commons.math3.stat.inference.OneWayAnova
import org.koin.dsl.module

val components = module {
    factory { InputValidator() }
    factory { UserManager(get()) }
    factory { PaginationManager() }
    factory { PearsonsCorrelation() }
    factory { OneWayAnova() }
    factory { StatisticsCalculator(get(),get()) }
    factory { StatsDataProcessor(get()) }
    factory { StatsDataInitializer() }
}

val repository = module {
    factory<StatsRepository> { StatsRepositoryImpl(get(),get()) }
    factory<ReportsRepository> { ReportsRepositoryImpl(get()) }
    factory<UserRepository> { UserRepositoryImpl(get(),get()) }
}

val dao = module {
    factory<StatsDatasource> { StatsDatasourceImpl(get(),get()) }
    factory<LogsDatasource> { LogsDatasourceImpl(get(),get()) }
    factory<IrritationsDatasource> { IrritationsDatasourceImpl() }
    factory<AdditionalDataDatasource> { AdditionalDataDatasourceImpl() }
    factory<UserDatasource> { UserDatasourceImpl() }
}

val single = module {
    single { FirebaseAuth.getInstance() }
}
