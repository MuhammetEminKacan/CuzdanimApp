package com.mek.cuzdanimapp.presentation.auth.register

import com.mek.cuzdanimapp.domain.model.CurrencyType

data class RegisterState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val selectedCurrency: CurrencyType = CurrencyType.TRY,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorCode: String? = null
)