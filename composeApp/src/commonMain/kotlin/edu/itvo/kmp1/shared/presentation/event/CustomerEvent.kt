package edu.itvo.kmp1.shared.presentation.event

import edu.itvo.kmp1.shared.domain.model.Customer

sealed interface CustomerEvent {

    data class SaveCustomer(
        val customer: Customer
    ) : CustomerEvent

    data class DeleteCustomer(
        val id: String
    ) : CustomerEvent
}