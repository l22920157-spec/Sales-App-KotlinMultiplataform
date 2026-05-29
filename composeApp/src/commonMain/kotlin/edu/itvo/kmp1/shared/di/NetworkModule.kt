package edu.itvo.kmp1.shared.di

import edu.itvo.kmp1.shared.core.network.createHttpClient
import edu.itvo.kmp1.shared.data.remote.api.CustomerApiService
import edu.itvo.kmp1.shared.data.remote.api.ProductApiService
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single { ProductApiService(get()) }
    single { CustomerApiService(get()) }
}