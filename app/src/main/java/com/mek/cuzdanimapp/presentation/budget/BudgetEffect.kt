package com.mek.cuzdanimapp.presentation.budget

sealed class BudgetEffect {
    data class ShowError(val message: String) : BudgetEffect()
    data object Saved : BudgetEffect()
    data object Deleted : BudgetEffect()
}