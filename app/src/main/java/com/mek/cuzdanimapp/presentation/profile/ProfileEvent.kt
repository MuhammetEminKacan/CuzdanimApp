package com.mek.cuzdanimapp.presentation.profile

sealed class ProfileEvent {
    data object Load : ProfileEvent()
    data object ShowEditSheet : ProfileEvent()
    data object HideEditSheet : ProfileEvent()
    data object ShowPasswordSheet : ProfileEvent()
    data object HidePasswordSheet : ProfileEvent()
    data class FullNameChanged(val fullName: String) : ProfileEvent()
    data class CurrencyChanged(val currency: String) : ProfileEvent()
    data class OldPasswordChanged(val password: String) : ProfileEvent()
    data class NewPasswordChanged(val password: String) : ProfileEvent()
    data class ConfirmPasswordChanged(val password: String) : ProfileEvent()
    data object TogglePasswordVisibility : ProfileEvent()
    data object ToggleNewPasswordVisibility : ProfileEvent()
    data object SaveProfile : ProfileEvent()
    data object SavePassword : ProfileEvent()
    data object Logout : ProfileEvent()
    data object ShowDeleteSheet : ProfileEvent()
    data object HideDeleteSheet : ProfileEvent()
    data class DeletePasswordChanged(val password: String) : ProfileEvent()
    data object ToggleDeletePasswordVisibility : ProfileEvent()
    data object ConfirmDeleteAccount : ProfileEvent()
}