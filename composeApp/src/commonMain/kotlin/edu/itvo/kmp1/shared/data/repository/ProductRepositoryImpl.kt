package edu.itvo.kmp1.shared.data.repository

import edu.itvo.kmp1.shared.data.local.datasource.ProductLocalDataSource
import edu.itvo.kmp1.shared.data.remote.api.ProductApiService
import edu.itvo.kmp1.shared.data.remote.mapper.toDomain
import edu.itvo.kmp1.shared.data.remote.mapper.toDto
import edu.itvo.kmp1.shared.domain.model.Product
import edu.itvo.kmp1.shared.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(
    private val remoteApi: ProductApiService,
    private val localDb: ProductLocalDataSource
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> = flow {
        println("getProducts: Starting to fetch from API...")
        try {
            val response = remoteApi.getProducts()
            println("getProducts: Fetched successfully!")
            if (response.success) {
                val remoteProducts = response.data.map { it.toDomain() }
                localDb.replaceAll(remoteProducts)
            }
        } catch (e: Exception) {
            println("Error fetching products: ${e.message}")
        }
        println("getProducts: Emitting from local DB")
        emitAll(localDb.products)
    }

    override suspend fun findProductByCode(productCode: String): Product? {
        val localProduct = localDb.findProductByCode(productCode)
        if (localProduct != null) {
            return localProduct
        }

        return try {
            val remoteProduct = remoteApi.findProductByCode(productCode).toDomain()
            localDb.saveProduct(remoteProduct)
            remoteProduct
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveProduct(product: Product) {
        try {
            val response = remoteApi.saveProduct(product.toDto())
            if (response.success) {
                localDb.saveProduct(response.data.toDomain())
            } else {
                localDb.saveProduct(product)
            }
        } catch (e: Exception) {
            localDb.saveProduct(product)
        }
    }

    override suspend fun updateProduct(product: Product) {
        try {
            val response = remoteApi.updateProduct(product.code, product.toDto())
            if (response.success) {
                localDb.saveProduct(response.data.toDomain())
            } else {
                localDb.saveProduct(product)
            }
        } catch (e: Exception) {
            localDb.saveProduct(product)
        }
    }

    override suspend fun deleteProduct(productCode: String) {
        try {
            remoteApi.deleteProduct(productCode)
        } catch (e: Exception) {
            println("Error deleting remote product: ${e.message}")
        }
        localDb.deleteProduct(productCode)
    }
}
