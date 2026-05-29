package edu.itvo.kmp1.shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.itvo.kmp1.shared.domain.model.Product
import edu.itvo.kmp1.shared.domain.usecase.product.CreateProductUseCase
import edu.itvo.kmp1.shared.domain.usecase.product.FindProductByCodeUseCase
import edu.itvo.kmp1.shared.domain.usecase.product.UpdateProductUseCase
import edu.itvo.kmp1.shared.presentation.state.ProductFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductFormViewModel(
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val findProductUseCase: FindProductByCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductFormState())
    val uiState: StateFlow<ProductFormState> = _uiState.asStateFlow()

    private var isEditing = false

    fun loadProduct(code: String?) {
        if (code == null) {
            isEditing = false
            return
        }
        isEditing = true
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val product = findProductUseCase(code)
            if (product != null) {
                _uiState.update {
                    it.copy(
                        code = product.code,
                        description = product.description,
                        category = product.category,
                        price = product.price.toString(),
                        stock = product.stock.toString(),
                        taxable = product.taxable,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Producto no encontrado") }
            }
        }
    }

    fun onEvent(event: ProductFormEvent) {
        when (event) {
            is ProductFormEvent.CodeChanged -> _uiState.update { it.copy(code = event.code) }
            is ProductFormEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.description) }
            is ProductFormEvent.CategoryChanged -> _uiState.update { it.copy(category = event.category) }
            is ProductFormEvent.PriceChanged -> _uiState.update { it.copy(price = event.price) }
            is ProductFormEvent.StockChanged -> _uiState.update { it.copy(stock = event.stock) }
            is ProductFormEvent.TaxableChanged -> _uiState.update { it.copy(taxable = event.taxable) }
            is ProductFormEvent.Save -> saveProduct()
        }
    }

    private fun saveProduct() {
        val state = _uiState.value
        val priceDouble = state.price.toDoubleOrNull()
        val stockInt = state.stock.toIntOrNull()

        if (state.code.isBlank() || state.description.isBlank() || priceDouble == null || stockInt == null) {
            _uiState.update { it.copy(error = "Verifica los datos (código, descripción, precio válido, stock válido)") }
            return
        }

        val product = Product(
            code = state.code,
            description = state.description,
            category = state.category,
            price = priceDouble,
            stock = stockInt,
            taxable = state.taxable
        )

        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                if (isEditing) {
                    updateProductUseCase(product)
                } else {
                    createProductUseCase(product)
                }
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al guardar") }
            }
        }
    }
}

sealed interface ProductFormEvent {
    data class CodeChanged(val code: String) : ProductFormEvent
    data class DescriptionChanged(val description: String) : ProductFormEvent
    data class CategoryChanged(val category: String) : ProductFormEvent
    data class PriceChanged(val price: String) : ProductFormEvent
    data class StockChanged(val stock: String) : ProductFormEvent
    data class TaxableChanged(val taxable: Boolean) : ProductFormEvent
    data object Save : ProductFormEvent
}
