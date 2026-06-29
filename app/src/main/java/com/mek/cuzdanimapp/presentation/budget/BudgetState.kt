package com.mek.cuzdanimapp.presentation.budget

import com.mek.cuzdanimapp.domain.model.Budget

data class BudgetState(
    val isLoading: Boolean = false,
    val budgets: List<Budget> = emptyList(),
    val errorMessage: String? = null,
    val isAddSheetVisible: Boolean = false,
    val editingBudget: Budget? = null,
    val selectedCategory: String = "",
    val limitAmount: String = "",
    val sheetErrorMessage: String? = null,
    val isSheetLoading: Boolean = false
)