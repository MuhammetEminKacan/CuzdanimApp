package com.mek.cuzdanimapp.presentation.transaction

sealed class TransactionEvent {
    data object LoadTransactions : TransactionEvent()
    data class TypeFilterChanged(val type: String) : TransactionEvent()
    data class CategoryFilterChanged(val category: String) : TransactionEvent()
    data class DeleteTransaction(val id: Long) : TransactionEvent()
    data object Refresh : TransactionEvent()
}