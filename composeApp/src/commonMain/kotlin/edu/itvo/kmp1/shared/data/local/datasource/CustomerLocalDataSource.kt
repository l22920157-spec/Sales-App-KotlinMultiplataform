package edu.itvo.kmp1.shared.data.local.datasource

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import edu.itvo.kmp1.database.SalesDatabase
import edu.itvo.kmp1.shared.domain.model.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import kotlin.collections.emptyList

class CustomerLocalDataSource(db: SalesDatabase) {
    private val queries = db.customerEntityQueries

    val customers: Flow<List<Customer>> = flow {
        // Simple polling to avoid mapToList(Dispatchers.Default) issues with WebWorkerDriver
        while (true) {
            val entities = try {
                queries.selectAll().awaitAsList()
            } catch (e: Exception) {
                emptyList()
            }
            emit(entities.map {
                Customer(
                    id = it.id,
                    name = it.name,
                    email = it.email,
                    purchaseHistory = it.purchaseHistory.toInt()
                )
            })
            delay(2000)
        }
    }

    suspend fun saveCustomer(customer: Customer) {
        queries.insertOrReplace(
            id = customer.id,
            name = customer.name,
            email = customer.email,
            purchaseHistory = customer.purchaseHistory.toLong()
        )
    }

    suspend fun deleteCustomer(id: String) {
        queries.deleteById(id)
    }

    suspend fun findCustomerById(id: String): Customer? {
        val entity = queries.findById(id).awaitAsOneOrNull()
        return entity?.let {
            Customer(
                id = it.id,
                name = it.name,
                email = it.email,
                purchaseHistory = it.purchaseHistory.toInt()
            )
        }
    }

    suspend fun replaceAll(newCustomers: List<Customer>) {
        val currentIds = queries.selectAll().awaitAsList().map { it.id }
        currentIds.forEach { deleteCustomer(it) }
        newCustomers.forEach { saveCustomer(it) }
    }
}