package edu.itvo.kmp1.shared.domain.usecase.product

import edu.itvo.kmp1.shared.domain.repository.ProductRepository

class DeleteProductUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(productCode: String) {
        repository.deleteProduct(productCode)
    }
}