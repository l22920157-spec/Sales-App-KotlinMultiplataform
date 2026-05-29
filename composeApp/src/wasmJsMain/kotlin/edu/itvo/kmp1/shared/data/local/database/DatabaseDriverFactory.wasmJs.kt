package edu.itvo.kmp1.shared.data.local.database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import edu.itvo.kmp1.database.SalesDatabase
import org.w3c.dom.Worker

private fun createWorker(): Worker = js("new Worker(new URL('@cashapp/sqldelight-sqljs-worker/sqljs.worker.js', import.meta.url))")

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return WebWorkerDriver(createWorker())
    }

    suspend fun createDatabase(): SalesDatabase {
        val driver = createDriver()
        SalesDatabase.Schema.awaitCreate(driver)
        return SalesDatabase(driver)
    }
}
