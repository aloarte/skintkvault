package com.skintker.di

import com.skintker.data.datasources.AdditionalDataDatasource
import com.skintker.data.datasources.IrritationsDatasource
import com.skintker.data.datasources.LogsDatasource
import com.skintker.data.datasources.impl.AdditionalDataDatasourceImpl
import com.skintker.data.datasources.impl.IrritationsDatasourceImpl
import com.skintker.data.datasources.impl.LogsDatasourceImpl
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.repository.ReportsRepositoryImpl
import com.skintker.data.validators.InputValidator
import org.koin.dsl.module

val validators = module {
    factory { InputValidator () }
}

val repository = module {
    factory<ReportsRepository> { ReportsRepositoryImpl(get()) }
}

val dao = module {
    factory<LogsDatasource> { LogsDatasourceImpl(get(),get()) }
    factory<IrritationsDatasource> { IrritationsDatasourceImpl() }
    factory<AdditionalDataDatasource> { AdditionalDataDatasourceImpl() }

}