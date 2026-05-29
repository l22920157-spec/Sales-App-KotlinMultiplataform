package edu.itvo.kmp1.shared.domain.usecase.product

import edu.itvo.kmp1.shared.domain.model.Product
import edu.itvo.kmp1.shared.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ListProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getProducts()
    }
}