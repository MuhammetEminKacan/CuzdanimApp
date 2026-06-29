package com.mek.cuzdanimapp.presentation.auth.register

sealed class RegisterEffect {
    data object NavigateToDashboard : RegisterEffect()
    data object NavigateToLogin : RegisterEffect()
    data class ShowError(val message: String) : RegisterEffect()
    data object ShowVerificationMessage : RegisterEffect()
}