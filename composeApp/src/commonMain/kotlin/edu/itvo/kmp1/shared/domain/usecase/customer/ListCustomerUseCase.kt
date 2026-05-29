package edu.itvo.kmp1.shared.domain.usecase.customer

import edu.itvo.kmp1.shared.domain.model.Customer
import edu.itvo.kmp1.shared.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow

class ListCustomerUseCase(private val repository: CustomerRepository) {
    operator fun invoke(): Flow<List<Customer>> {
        return repository.getCustomers()
    }
}