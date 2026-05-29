package edu.itvo.kmp1.shared.domain.repository

import edu.itvo.kmp1.shared.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun saveCustomer(customer: Customer)
    suspend fun updateCustomer(customer: Customer)
    suspend fun deleteCustomer(customerCode: String)
    suspend fun findCustomerByCode(customerCode: String): Customer?
    fun getCustomers(): Flow<List<Customer>>
}