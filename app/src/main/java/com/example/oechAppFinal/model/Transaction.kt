package com.example.oechAppFinal.model

data class Transaction(
    val amount: String,
    val description: String,
    val date: String,
    val isIncome: Boolean
)