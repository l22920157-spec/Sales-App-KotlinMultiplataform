package edu.itvo.kmp1.shared.data.remote.mapper

import edu.itvo.kmp1.shared.data.remote.dto.CustomerDto
import edu.itvo.kmp1.shared.domain.model.Customer

fun CustomerDto.toDomain(): Customer {
    return Customer(
        id = this.id,
        name = this.name,
        email = this.email,
        purchaseHistory = this.purchaseHistory.size
    )
}

fun Customer.toDto(): CustomerDto {
    return CustomerDto(
        id = this.id,
        name = this.name,
        email = this.email,
        purchaseHistory = List(this.purchaseHistory) { "Order ID: $it" }
    )
}