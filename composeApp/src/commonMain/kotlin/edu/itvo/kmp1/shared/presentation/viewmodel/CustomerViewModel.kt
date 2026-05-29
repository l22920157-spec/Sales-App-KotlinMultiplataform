package edu.itvo.kmp1.shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.itvo.kmp1.shared.domain.model.Customer
import edu.itvo.kmp1.shared.domain.usecase.customer.DeleteCustomerUseCase
import edu.itvo.kmp1.shared.domain.usecase.customer.ListCustomerUseCase
import edu.itvo.kmp1.shared.presentation.state.CustomerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val getCustomersUseCase: ListCustomerUseCase,
    private val deleteCustomerUseCase: DeleteCustomerUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    val uiState: StateFlow<CustomerUiState> =
        combine(getCustomersUseCase(), _searchQuery) { customers, query ->
            val filteredCustomers = if (query.isBlank()) {
                customers
            } else {
                customers.filter {
                    it.name.contains(query, ignoreCase = true) || it.id.contains(query, ignoreCase = true)
                }
            }
            CustomerUiState(
                isLoading = false,
                customers = filteredCustomers,
                searchQuery = query
            )
        }
            .onStart {
                emit(CustomerUiState(isLoading = true))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                CustomerUiState()
            )

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            deleteCustomerUseCase(customer.id)
        }
    }
}