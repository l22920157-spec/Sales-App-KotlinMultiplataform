package edu.itvo.kmp1.shared.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("code") val code: String,
    @SerialName("description") val description: String,
    @SerialName("category") val category: String,
    @SerialName("price") val price: Double,
    @SerialName("stock") val stock: Int,
    @SerialName("taxable") val taxable: Boolean = true
)