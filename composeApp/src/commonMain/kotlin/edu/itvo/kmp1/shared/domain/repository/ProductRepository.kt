package edu.itvo.kmp1.shared.domain.repository

import edu.itvo.kmp1.shared.domain.model.Product
import kotlinx.coroutines.flow.Flow


interface ProductRepository {
    suspend fun saveProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(productCode: String)
    suspend fun findProductByCode(productCode: String): Product?
    fun getProducts(): Flow<List<Product>>
}