package com.mek.cuzdanimapp.presentation.main

data class AddTransactionSheetState(
    val isVisible: Boolean = false,
    val selectedType: String = "EXPENSE",
    val selectedCategory: String = "",
    val amount: String = "",
    val description: String = "",
    val transactionDate: String = java.time.LocalDate.now().toString(),
    val isRecurring: Boolean = false,
    val frequency: String = "MONTHLY",
    val startDate: String = java.time.LocalDate.now().toString(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)