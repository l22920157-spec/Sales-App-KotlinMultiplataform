package edu.itvo.kmp1.shared.data.remote.mapper

import edu.itvo.kmp1.shared.data.remote.dto.ProductDto
import edu.itvo.kmp1.shared.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        code = this.code,
        description = this.description,
        category = this.category,
        price = this.price,
        stock = this.stock,
        taxable = this.taxable
    )
}

fun Product.toDto(): ProductDto {
    return ProductDto(
        code = this.code,
        description = this.description,
        category = this.category,
        price = this.price,
        stock = this.stock,
        taxable = this.taxable
    )
}