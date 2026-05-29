package edu.itvo.kmp1.shared.domain.usecase.customer

import edu.itvo.kmp1.shared.domain.model.Customer
import edu.itvo.kmp1.shared.domain.repository.CustomerRepository

class UpdateCustomerCase(private val repository: CustomerRepository) {
    suspend operator fun invoke(customer: Customer) {
        repository.updateCustomer(customer)
    }
}