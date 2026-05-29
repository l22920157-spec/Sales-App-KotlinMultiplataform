package edu.itvo.kmp1.shared.domain.usecase.product

import edu.itvo.kmp1.shared.domain.model.Product
import edu.itvo.kmp1.shared.domain.repository.ProductRepository

class UpdateProductUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(product: Product) {
        repository.updateProduct(product)
    }
}
