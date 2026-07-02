package com.mek.cuzdanimapp.presentation.auth.register

import com.mek.cuzdanimapp.domain.model.CurrencyType

sealed class RegisterEvent {
    data class FullNameChanged(val fullName: String) : RegisterEvent()
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent()
    data class CurrencySelected(val currency: CurrencyType) : RegisterEvent()
    data object TogglePasswordVisibility : RegisterEvent()
    data object ToggleConfirmPasswordVisibility : RegisterEvent()
    data object RegisterClicked : RegisterEvent()
    data object NavigateToLogin : RegisterEvent()
    data object ClearState : RegisterEvent()
}