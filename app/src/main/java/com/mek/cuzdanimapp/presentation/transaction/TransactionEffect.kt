package com.mek.cuzdanimapp.presentation.transaction

sealed class TransactionEffect {
    data class ShowError(val message: String) : TransactionEffect()
    data object TransactionDeleted : TransactionEffect()
}