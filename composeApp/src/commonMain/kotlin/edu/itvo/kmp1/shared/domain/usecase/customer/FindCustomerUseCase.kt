package edu.itvo.kmp1.shared.domain.usecase.customer

import edu.itvo.kmp1.shared.domain.model.Customer
import edu.itvo.kmp1.shared.domain.repository.CustomerRepository

class FindCustomerUseCase(private val repository: CustomerRepository) {
    suspend operator fun invoke(customerCode: String): Customer? {
        return repository.findCustomerByCode(customerCode)
    }
}