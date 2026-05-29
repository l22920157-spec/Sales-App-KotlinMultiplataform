package edu.itvo.kmp1.shared.domain.usecase.customer

import edu.itvo.kmp1.shared.domain.model.Customer
import edu.itvo.kmp1.shared.domain.repository.CustomerRepository

class CreateCustomerUseCase(private val repository: CustomerRepository) {
    suspend operator fun invoke(customer: Customer) {
        repository.saveCustomer(customer)
    }
}