package com.mek.cuzdanimapp.presentation.main

sealed class AddTransactionSheetEvent {
    data object Show : AddTransactionSheetEvent()
    data object Hide : AddTransactionSheetEvent()
    data class TypeChanged(val type: String) : AddTransactionSheetEvent()
    data class CategoryChanged(val category: String) : AddTransactionSheetEvent()
    data class AmountChanged(val amount: String) : AddTransactionSheetEvent()
    data class DescriptionChanged(val description: String) : AddTransactionSheetEvent()
    data class DateChanged(val date: String) : AddTransactionSheetEvent()
    data object SaveClicked : AddTransactionSheetEvent()
}