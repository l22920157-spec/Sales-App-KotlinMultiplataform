package edu.itvo.kmp1.shared.data.remote.api

import edu.itvo.kmp1.shared.core.constants.Constants
import edu.itvo.kmp1.shared.core.network.ApiResponse
import edu.itvo.kmp1.shared.data.remote.dto.ProductDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProductApiService(private val client: HttpClient) {

    suspend fun getProducts(): ApiResponse<List<ProductDto>> {
        return client.get("${Constants.BASE_URL}products").body()
    }

    suspend fun findProductByCode(code: String): ProductDto {
        return client.get("${Constants.BASE_URL}products/$code").body()
    }

    suspend fun saveProduct(product: ProductDto): ApiResponse<ProductDto> {
        return client.post("${Constants.BASE_URL}products") {
            contentType(ContentType.Application.Json)
            setBody(product)
        }.body()
    }

    suspend fun updateProduct(code: String, product: ProductDto): ApiResponse<ProductDto> {
        return client.put("${Constants.BASE_URL}products/$code") {
            contentType(ContentType.Application.Json)
            setBody(product)
        }.body()
    }

    suspend fun deleteProduct(code: String) {
        client.delete("${Constants.BASE_URL}products/$code")
    }
}