package edu.itvo.kmp1.shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.itvo.kmp1.shared.domain.model.Product
import edu.itvo.kmp1.shared.domain.usecase.product.DeleteProductUseCase
import edu.itvo.kmp1.shared.domain.usecase.product.ListProductsUseCase
import edu.itvo.kmp1.shared.presentation.state.ProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(
    private val getProductsUseCase: ListProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    val uiState: StateFlow<ProductUiState> =
        combine(getProductsUseCase(), _searchQuery) { products, query ->
            val filteredProducts = if (query.isBlank()) {
                products
            } else {
                products.filter {
                    it.description.contains(query, ignoreCase = true)
                            || it.code.contains(query, ignoreCase = true)
                }
            }
            ProductUiState(
                isLoading = false,
                products = filteredProducts,
                searchQuery = query
            )
        }
            .onStart {
                emit(ProductUiState(isLoading = true))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                ProductUiState()
            )

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            deleteProductUseCase(product.code)
        }
    }
}
