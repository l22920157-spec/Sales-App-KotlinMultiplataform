package edu.itvo.kmp1.shared.presentation.state

data class ProductFormState(
    val code: String = "",
    val description: String = "",
    val category: String = "",
    val price: String = "",
    val stock: String = "",
    val taxable: Boolean = true,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)
