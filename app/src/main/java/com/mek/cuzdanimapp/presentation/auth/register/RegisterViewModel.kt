package com.mek.cuzdanimapp.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.domain.usecase.auth.RegisterUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _effect = Channel<RegisterEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.FullNameChanged -> {
                _state.update { it.copy(fullName = event.fullName, errorMessage = null) }
            }
            is RegisterEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, errorMessage = null) }
            }
            is RegisterEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, errorMessage = null) }
            }
            is RegisterEvent.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = event.confirmPassword, errorMessage = null) }
            }
            is RegisterEvent.CurrencySelected -> {
                _state.update { it.copy(selectedCurrency = event.currency) }
            }
            is RegisterEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is RegisterEvent.ToggleConfirmPasswordVisibility -> {
                _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }
            is RegisterEvent.RegisterClicked -> register()
            is RegisterEvent.NavigateToLogin -> {
                viewModelScope.launch {
                    _effect.send(RegisterEffect.NavigateToLogin)
                }
            }
        }
    }

    private fun register() {
        val state = _state.value

        if (state.fullName.isBlank()) {
            _state.update { it.copy(errorMessage = "Ad soyad boş bırakılamaz") }
            return
        }
        if (state.email.isBlank()) {
            _state.update { it.copy(errorMessage = "E-posta boş bırakılamaz") }
            return
        }
        if (state.password.length < 8) {
            _state.update { it.copy(errorMessage = "Şifre en az 8 karakter olmalı") }
            return
        }
        if (state.password != state.confirmPassword) {
            _state.update { it.copy(errorMessage = "Şifreler eşleşmiyor") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = registerUseCase(
                fullName = state.fullName,
                email = state.email,
                password = state.password,
                currency = state.selectedCurrency
            )) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(RegisterEffect.NavigateToDashboard)
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    _effect.send(RegisterEffect.ShowError(result.message ?: "Bir hata oluştu"))
                }
                is Resource.Loading -> Unit
            }
        }
    }
}