package edu.itvo.kmp1.shared.data.local.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import edu.itvo.kmp1.database.SalesDatabase


import app.cash.sqldelight.async.coroutines.synchronous

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SalesDatabase.Schema.synchronous(), context, "sales.db")
    }
}
