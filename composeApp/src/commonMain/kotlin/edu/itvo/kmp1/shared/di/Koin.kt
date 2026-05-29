package edu.itvo.kmp1.shared.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration? = null) {
    startKoin {
        if (appDeclaration != null) {
            appDeclaration()
        }
        modules(
            platformModule(),
            networkModule,
            repositoryModule,
            useCaseModule,
            viewModelModule
        )
    }
}


