package com.mek.cuzdanimapp.presentation.splash

sealed class SplashEffect {
    data object NavigateToDashboard : SplashEffect()
    data object NavigateToLogin : SplashEffect()
}