package edu.itvo.kmp1.shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.itvo.kmp1.shared.domain.model.Customer
import edu.itvo.kmp1.shared.domain.usecase.customer.CreateCustomerUseCase
import edu.itvo.kmp1.shared.domain.usecase.customer.FindCustomerUseCase
import edu.itvo.kmp1.shared.domain.usecase.customer.UpdateCustomerCase
import edu.itvo.kmp1.shared.presentation.state.CustomerFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomerFormViewModel(
    private val createCustomerUseCase: CreateCustomerUseCase,
    private val updateCustomerUseCase: UpdateCustomerCase,
    private val findCustomerUseCase: FindCustomerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerFormState())
    val uiState: StateFlow<CustomerFormState> = _uiState.asStateFlow()

    private var isEditing = false

    fun loadCustomer(id: String?) {
        if (id == null) {
            isEditing = false
            return
        }
        isEditing = true
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val customer = findCustomerUseCase(id)
            if (customer != null) {
                _uiState.update {
                    it.copy(
                        id = customer.id,
                        name = customer.name,
                        email = customer.email,
                        purchaseHistory = customer.purchaseHistory.toString(),
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Cliente no encontrado") }
            }
        }
    }

    fun onEvent(event: CustomerFormEvent) {
        when (event) {
            is CustomerFormEvent.IdChanged -> _uiState.update { it.copy(id = event.id) }
            is CustomerFormEvent.NameChanged -> _uiState.update { it.copy(name = event.name) }
            is CustomerFormEvent.EmailChanged -> _uiState.update { it.copy(email = event.email) }
            is CustomerFormEvent.HistoryChanged -> _uiState.update { it.copy(purchaseHistory = event.history) }
            is CustomerFormEvent.Save -> saveCustomer()
        }
    }

    private fun saveCustomer() {
        val state = _uiState.value
        if (state.id.isBlank() || state.name.isBlank()) {
            _uiState.update { it.copy(error = "El ID y Nombre son obligatorios") }
            return
        }

        val customer = Customer(
            id = state.id,
            name = state.name,
            email = state.email,
            purchaseHistory = state.purchaseHistory.toIntOrNull() ?: 0
        )

        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                if (isEditing) {
                    updateCustomerUseCase(customer)
                } else {
                    createCustomerUseCase(customer)
                }
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al guardar") }
            }
        }
    }
}

sealed interface CustomerFormEvent {
    data class IdChanged(val id: String) : CustomerFormEvent
    data class NameChanged(val name: String) : CustomerFormEvent
    data class EmailChanged(val email: String) : CustomerFormEvent
    data class HistoryChanged(val history: String) : CustomerFormEvent
    data object Save : CustomerFormEvent
}
