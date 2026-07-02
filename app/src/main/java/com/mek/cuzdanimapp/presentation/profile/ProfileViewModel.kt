package com.mek.cuzdanimapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.data.local.LanguagePreferences
import com.mek.cuzdanimapp.data.local.ThemePreferences
import com.mek.cuzdanimapp.data.local.TokenManager
import com.mek.cuzdanimapp.domain.usecase.profile.ChangePasswordUseCase
import com.mek.cuzdanimapp.domain.usecase.profile.DeleteAccountUseCase
import com.mek.cuzdanimapp.domain.usecase.profile.GetProfileUseCase
import com.mek.cuzdanimapp.domain.usecase.profile.UpdateProfileUseCase
import com.mek.cuzdanimapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val tokenManager: TokenManager,
    private val themePreferences: ThemePreferences,
    private val languagePreferences: LanguagePreferences
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _effect = Channel<ProfileEffect>()
    val effect = _effect.receiveAsFlow()

    val isDarkMode = themePreferences.isDarkMode
    val currentLanguage = languagePreferences.languageCode

    init {
        onEvent(ProfileEvent.Load)
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Load -> load()

            is ProfileEvent.ShowEditSheet -> {
                _state.update {
                    it.copy(
                        isEditSheetVisible = true,
                        fullName = it.user?.fullName ?: "",
                        selectedCurrency = it.user?.currency?.name ?: "TRY",
                        sheetErrorCode = null,
                        sheetErrorMessage = null
                    )
                }
            }

            is ProfileEvent.HideEditSheet -> {
                _state.update { it.copy(isEditSheetVisible = false) }
            }

            is ProfileEvent.ShowPasswordSheet -> {
                _state.update {
                    it.copy(
                        isPasswordSheetVisible = true,
                        oldPassword = "",
                        newPassword = "",
                        confirmPassword = "",
                        sheetErrorCode = null,
                        sheetErrorMessage = null
                    )
                }
            }

            is ProfileEvent.HidePasswordSheet -> {
                _state.update { it.copy(isPasswordSheetVisible = false) }
            }

            is ProfileEvent.ShowDeleteSheet -> {
                _state.update {
                    it.copy(
                        isDeleteSheetVisible = true,
                        deletePassword = "",
                        sheetErrorCode = null,
                        sheetErrorMessage = null
                    )
                }
            }

            is ProfileEvent.HideDeleteSheet -> {
                _state.update { it.copy(isDeleteSheetVisible = false) }
            }

            is ProfileEvent.FullNameChanged -> {
                _state.update { it.copy(fullName = event.fullName, sheetErrorCode = null, sheetErrorMessage = null) }
            }

            is ProfileEvent.CurrencyChanged -> {
                _state.update { it.copy(selectedCurrency = event.currency) }
            }

            is ProfileEvent.OldPasswordChanged -> {
                _state.update { it.copy(oldPassword = event.password, sheetErrorCode = null, sheetErrorMessage = null) }
            }

            is ProfileEvent.NewPasswordChanged -> {
                _state.update { it.copy(newPassword = event.password, sheetErrorCode = null, sheetErrorMessage = null) }
            }

            is ProfileEvent.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = event.password, sheetErrorCode = null, sheetErrorMessage = null) }
            }

            is ProfileEvent.DeletePasswordChanged -> {
                _state.update { it.copy(deletePassword = event.password, sheetErrorCode = null, sheetErrorMessage = null) }
            }

            is ProfileEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is ProfileEvent.ToggleNewPasswordVisibility -> {
                _state.update { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }
            }

            is ProfileEvent.ToggleDeletePasswordVisibility -> {
                _state.update { it.copy(isDeletePasswordVisible = !it.isDeletePasswordVisible) }
            }

            is ProfileEvent.SaveProfile -> saveProfile()
            is ProfileEvent.SavePassword -> savePassword()
            is ProfileEvent.Logout -> logout()
            is ProfileEvent.ConfirmDeleteAccount -> deleteAccount()
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            themePreferences.setDarkMode(enabled)
        }
    }

    fun setLanguage(code: String?) {
        viewModelScope.launch {
            languagePreferences.setLanguage(code)
            _effect.send(ProfileEffect.LanguageChanged)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getProfileUseCase()) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, user = result.data) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(ProfileEffect.ShowError(result.message ?: ""))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun saveProfile() {
        val state = _state.value

        if (state.fullName.isBlank()) {
            _state.update { it.copy(sheetErrorCode = "LOCAL_FULLNAME_EMPTY") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSheetLoading = true, sheetErrorCode = null, sheetErrorMessage = null) }
            when (val result = updateProfileUseCase(state.fullName, state.selectedCurrency)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isSheetLoading = false,
                            isEditSheetVisible = false,
                            user = result.data
                        )
                    }
                    _effect.send(ProfileEffect.ProfileUpdated)
                }
                is Resource.Error -> {
                    val code = result.message ?: "UNKNOWN"
                    _state.update {
                        it.copy(
                            isSheetLoading = false,
                            sheetErrorCode = code
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun savePassword() {
        val state = _state.value

        if (state.oldPassword.isBlank()) {
            _state.update { it.copy(sheetErrorCode = "LOCAL_OLD_PASSWORD_EMPTY") }
            return
        }
        if (state.newPassword.isBlank()) {
            _state.update { it.copy(sheetErrorCode = "LOCAL_NEW_PASSWORD_EMPTY") }
            return
        }
        if (state.newPassword.length < 8) {
            _state.update { it.copy(sheetErrorCode = "LOCAL_PASSWORD_SHORT") }
            return
        }
        if (state.newPassword != state.confirmPassword) {
            _state.update { it.copy(sheetErrorCode = "LOCAL_PASSWORD_MISMATCH") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSheetLoading = true, sheetErrorCode = null, sheetErrorMessage = null) }
            when (val result = changePasswordUseCase(
                state.oldPassword, state.newPassword, state.confirmPassword
            )) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(isSheetLoading = false, isPasswordSheetVisible = false)
                    }
                    _effect.send(ProfileEffect.PasswordChanged)
                }
                is Resource.Error -> {
                    val code = result.message ?: "UNKNOWN"
                    _state.update {
                        it.copy(
                            isSheetLoading = false,
                            sheetErrorCode = code
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun deleteAccount() {
        val state = _state.value

        if (state.deletePassword.isBlank()) {
            _state.update { it.copy(sheetErrorCode = "LOCAL_DELETE_PASSWORD_EMPTY") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSheetLoading = true, sheetErrorCode = null, sheetErrorMessage = null) }
            when (val result = deleteAccountUseCase(state.deletePassword)) {
                is Resource.Success -> {
                    tokenManager.clearTokens()
                    _state.update { it.copy(isSheetLoading = false, isDeleteSheetVisible = false) }
                    _effect.send(ProfileEffect.AccountDeleted)
                }
                is Resource.Error -> {
                    val code = result.message ?: "UNKNOWN"
                    _state.update {
                        it.copy(
                            isSheetLoading = false,
                            sheetErrorCode = code
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun logout() {
        tokenManager.clearTokens()
        viewModelScope.launch {
            _effect.send(ProfileEffect.LoggedOut)
        }
    }
}