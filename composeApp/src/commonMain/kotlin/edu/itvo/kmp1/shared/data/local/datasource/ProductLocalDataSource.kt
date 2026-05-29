package edu.itvo.kmp1.shared.data.local.datasource

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import edu.itvo.kmp1.database.SalesDatabase
import edu.itvo.kmp1.shared.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay

class ProductLocalDataSource(db: SalesDatabase) {
    private val queries = db.productEntityQueries

    val products: Flow<List<Product>> = flow {
        // Simple polling to avoid mapToList(Dispatchers.Default) issues with WebWorkerDriver
        while (true) {
            val entities = try {
                queries.selectAll().awaitAsList()
            } catch (e: Exception) {
                emptyList()
            }
            emit(entities.map {
                Product(
                    code = it.code,
                    description = it.description,
                    category = it.category,
                    price = it.price,
                    stock = it.stock.toInt(),
                    taxable = it.taxable == 1L
                )
            })
            delay(2000)
        }
    }

    suspend fun saveProduct(product: Product) {
        queries.insertOrReplace(
            code = product.code,
            description = product.description,
            category = product.category,
            price = product.price,
            stock = product.stock.toLong(),
            taxable = if (product.taxable) 1L else 0L
        )
    }

    suspend fun deleteProduct(code: String) {
        queries.deleteByCode(code)
    }

    suspend fun findProductByCode(code: String): Product? {
        val entity = queries.findByCode(code).awaitAsOneOrNull()
        return entity?.let {
            Product(
                code = it.code,
                description = it.description,
                category = it.category,
                price = it.price,
                stock = it.stock.toInt(),
                taxable = it.taxable == 1L
            )
        }
    }

    suspend fun replaceAll(newProducts: List<Product>) {
        val currentCodes = queries.selectAll().awaitAsList().map { it.code }
        currentCodes.forEach { deleteProduct(it) }
        newProducts.forEach { saveProduct(it) }
    }
}
