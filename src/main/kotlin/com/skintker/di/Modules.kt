package com.skintker.di

import com.google.firebase.auth.FirebaseAuth
import com.skintker.data.datasources.AdditionalDataDatasource
import com.skintker.data.datasources.IrritationsDatasource
import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.datasources.UserDatasource
import com.skintker.data.datasources.impl.AdditionalDataDatasourceImpl
import com.skintker.data.datasources.impl.IrritationsDatasourceImpl
import com.skintker.data.datasources.impl.LogsDatasourceImpl
import com.skintker.data.datasources.impl.UserDatasourceImpl
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.repository.impl.ReportsRepositoryImpl
import com.skintker.data.validators.InputValidator
import com.skintker.domain.repository.UserRepository
import com.skintker.domain.repository.impl.UserRepositoryImpl
import org.koin.dsl.module

val validators = module {
    factory { InputValidator(get()) }
}

val repository = module {
    factory<ReportsRepository> { ReportsRepositoryImpl(get()) }
    factory<UserRepository> { UserRepositoryImpl(get(),get()) }

}

val dao = module {
    factory<LogsDatasource> { LogsDatasourceImpl(get(),get()) }
    factory<IrritationsDatasource> { IrritationsDatasourceImpl() }
    factory<AdditionalDataDatasource> { AdditionalDataDatasourceImpl() }
    factory<UserDatasource> { UserDatasourceImpl() }
}

val single = module {
    single { FirebaseAuth.getInstance() }
}