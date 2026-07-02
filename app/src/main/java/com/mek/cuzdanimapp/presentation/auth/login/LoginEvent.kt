package com.mek.cuzdanimapp.presentation.auth.login

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data object TogglePasswordVisibility : LoginEvent()
    data object LoginClicked : LoginEvent()
    data object NavigateToRegister : LoginEvent()
    data object ClearState : LoginEvent()
}