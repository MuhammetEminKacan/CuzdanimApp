package com.mek.cuzdanimapp.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.domain.usecase.auth.LoginUseCase
import com.mek.cuzdanimapp.domain.usecase.auth.ResendVerificationUseCase
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val resendVerificationUseCase: ResendVerificationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, errorMessage = null) }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, errorMessage = null) }
            }
            is LoginEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is LoginEvent.LoginClicked -> login()
            is LoginEvent.NavigateToRegister -> {
                viewModelScope.launch {
                    _effect.send(LoginEffect.NavigateToRegister)
                }
            }
            is LoginEvent.ResendVerification -> {
                viewModelScope.launch {
                    resendVerificationUseCase(_state.value.email)
                    _effect.send(LoginEffect.VerificationResent)
                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = loginUseCase(
                email = _state.value.email,
                password = _state.value.password
            )) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(LoginEffect.NavigateToDashboard)
                }
                is Resource.Error -> {
                    val isNotVerified = result.message?.contains("7001") == true ||
                            result.message?.contains("not verified") == true

                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = if (isNotVerified)
                                "E-posta adresiniz doğrulanmamış."
                            else result.message,
                            showResendOption = isNotVerified
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}