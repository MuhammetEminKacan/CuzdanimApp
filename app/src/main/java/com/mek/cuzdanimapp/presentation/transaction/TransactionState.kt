package com.mek.cuzdanimapp.presentation.transaction

import com.mek.cuzdanimapp.domain.model.Transaction

data class TransactionState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val selectedTypeFilter: String = "ALL",
    val selectedCategoryFilter: String = "ALL",
    val errorMessage: String? = null
)
