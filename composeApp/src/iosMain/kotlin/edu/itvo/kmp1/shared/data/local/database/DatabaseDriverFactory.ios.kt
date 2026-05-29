package edu.itvo.kmp1.shared.data.local.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import edu.itvo.kmp1.database.SalesDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(SalesDatabase.Schema, "sales.db")
    }
}
