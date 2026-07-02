package com.mek.cuzdanimapp.presentation.auth.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val errorCode: String? = null
)
