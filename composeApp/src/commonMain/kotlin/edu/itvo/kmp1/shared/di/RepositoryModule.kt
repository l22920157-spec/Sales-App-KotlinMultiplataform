package edu.itvo.kmp1.shared.di

import edu.itvo.kmp1.database.SalesDatabase
import edu.itvo.kmp1.shared.data.local.datasource.CustomerLocalDataSource
import edu.itvo.kmp1.shared.data.local.datasource.ProductLocalDataSource
import edu.itvo.kmp1.shared.data.repository.CustomerRepositoryImpl
import edu.itvo.kmp1.shared.data.repository.ProductRepositoryImpl
import edu.itvo.kmp1.shared.domain.repository.CustomerRepository
import edu.itvo.kmp1.shared.domain.repository.ProductRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { CustomerLocalDataSource(get()) }
    single { ProductLocalDataSource(get()) }
    single<CustomerRepository> { CustomerRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
}