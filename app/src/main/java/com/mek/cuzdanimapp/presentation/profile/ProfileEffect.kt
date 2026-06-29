package com.mek.cuzdanimapp.presentation.profile

sealed class ProfileEffect {
    data class ShowError(val message: String) : ProfileEffect()
    data object ProfileUpdated : ProfileEffect()
    data object PasswordChanged : ProfileEffect()
    data object LoggedOut : ProfileEffect()
    data object AccountDeleted : ProfileEffect()
}