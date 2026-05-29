package edu.itvo.kmp1.shared.presentation.state

data class CustomerFormState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val purchaseHistory: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)
