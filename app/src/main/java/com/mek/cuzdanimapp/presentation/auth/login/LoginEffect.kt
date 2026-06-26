package com.mek.cuzdanimapp.presentation.auth.login

sealed class LoginEffect {
    data object NavigateToDashboard : LoginEffect()
    data object NavigateToRegister : LoginEffect()
    data class ShowError(val message: String) : LoginEffect()
}