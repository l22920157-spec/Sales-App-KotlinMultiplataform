package edu.itvo.kmp1.shared.presentation.state

import edu.itvo.kmp1.shared.domain.model.Product

data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)
