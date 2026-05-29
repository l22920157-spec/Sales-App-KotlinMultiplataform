package edu.itvo.kmp1.shared.data.remote.api

import edu.itvo.kmp1.shared.core.constants.Constants
import edu.itvo.kmp1.shared.data.remote.dto.CustomerDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CustomerApiService(private val client: HttpClient) {

    suspend fun getCustomers(): List<CustomerDto> {
        return client.get("${Constants.BASE_URL}customers").body()
    }

    suspend fun findCustomerById(id: String): CustomerDto {
        return client.get("${Constants.BASE_URL}customers/$id").body()
    }

    suspend fun saveCustomer(customer: CustomerDto): CustomerDto {
        return client.post("${Constants.BASE_URL}customers") {
            contentType(ContentType.Application.Json)
            setBody(customer)
        }.body()
    }

    suspend fun updateCustomer(id: String, customer: CustomerDto): CustomerDto {
        return client.put("${Constants.BASE_URL}customers/$id") {
            contentType(ContentType.Application.Json)
            setBody(customer)
        }.body()
    }

    suspend fun deleteCustomer(id: String) {
        client.delete("${Constants.BASE_URL}customers/$id")
    }
}