package edu.itvo.kmp1.shared.data.repository

import edu.itvo.kmp1.shared.data.local.datasource.CustomerLocalDataSource
import edu.itvo.kmp1.shared.data.remote.api.CustomerApiService
import edu.itvo.kmp1.shared.data.remote.mapper.toDomain
import edu.itvo.kmp1.shared.data.remote.mapper.toDto
import edu.itvo.kmp1.shared.domain.model.Customer
import edu.itvo.kmp1.shared.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class CustomerRepositoryImpl(
    private val remoteApi: CustomerApiService,
    private val localDb: CustomerLocalDataSource
) : CustomerRepository {

    override fun getCustomers(): Flow<List<Customer>> = flow {
        try {
            val remoteCustomers = remoteApi.getCustomers().map { it.toDomain() }
            localDb.replaceAll(remoteCustomers)
        } catch (e: Exception) {
            println("Error fetching customers: ${e.message}")
        }
        emitAll(localDb.customers)
    }

    override suspend fun findCustomerByCode(customerCode: String): Customer? {
        val localCustomer = localDb.findCustomerById(customerCode)
        if (localCustomer != null) {
            return localCustomer
        }

        return try {
            val remoteCustomer = remoteApi.findCustomerById(customerCode).toDomain()
            localDb.saveCustomer(remoteCustomer)
            remoteCustomer
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveCustomer(customer: Customer) {
        try {
            val saved = remoteApi.saveCustomer(customer.toDto()).toDomain()
            localDb.saveCustomer(saved)
        } catch (e: Exception) {
            localDb.saveCustomer(customer)
        }
    }

    override suspend fun updateCustomer(customer: Customer) {
        try {
            val updated = remoteApi.updateCustomer(customer.id, customer.toDto()).toDomain()
            localDb.saveCustomer(updated)
        } catch (e: Exception) {
            localDb.saveCustomer(customer)
        }
    }

    override suspend fun deleteCustomer(customerCode: String) {
        try {
            remoteApi.deleteCustomer(customerCode)
        } catch (e: Exception) {
            println("Error deleting remote customer: ${e.message}")
        }
        localDb.deleteCustomer(customerCode)
    }
}
