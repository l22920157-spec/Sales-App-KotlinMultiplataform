package edu.itvo.kmp1.shared.di

import app.cash.sqldelight.async.coroutines.synchronous
import edu.itvo.kmp1.database.SalesDatabase
import edu.itvo.kmp1.shared.data.local.database.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory(get()) }
    single {
        val driver = get<DatabaseDriverFactory>().createDriver()
        SalesDatabase(driver)
    }
}
