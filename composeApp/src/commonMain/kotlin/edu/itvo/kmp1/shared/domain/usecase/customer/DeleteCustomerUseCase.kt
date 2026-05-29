package edu.itvo.kmp1.shared.domain.usecase.customer

import edu.itvo.kmp1.shared.domain.repository.CustomerRepository

class DeleteCustomerUseCase(private val repository: CustomerRepository) {
    suspend operator fun invoke(customerCode: String) {
        repository.deleteCustomer(customerCode)
    }
}