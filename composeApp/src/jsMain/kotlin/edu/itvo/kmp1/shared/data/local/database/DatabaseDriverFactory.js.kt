package edu.itvo.kmp1.shared.data.local.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = WebWorkerDriver(
            Worker(
                js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
            )
        )
        
        // Ejecutamos la creacion de tablas sincronamente para que se encolen 
        // ANTES de que cualquier repositorio intente hacer un SELECT.
        driver.execute(null, "CREATE TABLE IF NOT EXISTS ProductEntity (code TEXT NOT NULL PRIMARY KEY, description TEXT NOT NULL, category TEXT NOT NULL, price REAL NOT NULL, stock INTEGER NOT NULL, taxable INTEGER NOT NULL);", 0, null)
        driver.execute(null, "CREATE TABLE IF NOT EXISTS CustomerEntity (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, email TEXT NOT NULL, purchaseHistory INTEGER NOT NULL);", 0, null)

        return driver
    }
}
