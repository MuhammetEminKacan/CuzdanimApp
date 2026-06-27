package com.mek.cuzdanimapp.presentation.main

sealed class AddTransactionSheetEffect {
    data object TransactionAdded : AddTransactionSheetEffect()
    data class ShowError(val message: String) : AddTransactionSheetEffect()
}