package edu.itvo.kmp1.shared.domain.model

data class Customer (
    val id: String,
    val name: String,
    val email: String,
    val purchaseHistory: Int
)