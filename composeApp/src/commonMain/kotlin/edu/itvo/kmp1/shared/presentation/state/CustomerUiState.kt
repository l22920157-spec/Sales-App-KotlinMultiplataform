package edu.itvo.kmp1.shared.presentation.state

import edu.itvo.kmp1.shared.domain.model.Customer

data class CustomerUiState(
    val isLoading: Boolean = false,
    val customers: List<Customer> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)