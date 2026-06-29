package com.mek.cuzdanimapp.presentation.budget

import com.mek.cuzdanimapp.domain.model.Budget

sealed class BudgetEvent {
    data object Load : BudgetEvent()
    data object Refresh : BudgetEvent()
    data object ShowAddSheet : BudgetEvent()
    data class ShowEditSheet(val budget: Budget) : BudgetEvent()
    data object HideSheet : BudgetEvent()
    data class CategoryChanged(val category: String) : BudgetEvent()
    data class LimitChanged(val limit: String) : BudgetEvent()
    data object Save : BudgetEvent()
    data class Delete(val id: Long) : BudgetEvent()
}