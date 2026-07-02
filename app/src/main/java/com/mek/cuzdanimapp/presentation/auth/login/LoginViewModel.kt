package com.mek.cuzdanimapp.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.domain.usecase.auth.LoginUseCase
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
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, errorCode = null, errorMessage = null) }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, errorCode = null, errorMessage = null) }
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
            is LoginEvent.ClearState -> {
                _state.update { LoginState() }
            }
        }
    }

    private fun login() {
        val state = _state.value

        if (state.email.isBlank()) {
            _state.update { it.copy(errorCode = "LOCAL_EMAIL_EMPTY", errorMessage = null) }
            return
        }
        if (state.password.isBlank()) {
            _state.update { it.copy(errorCode = "LOCAL_PASSWORD_EMPTY", errorMessage = null) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorCode = null, errorMessage = null) }

            when (val result = loginUseCase(
                email = state.email,
                password = state.password
            )) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(LoginEffect.NavigateToDashboard)
                }
                is Resource.Error -> {
                    val code = result.message ?: "UNKNOWN"
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorCode = code,
                            errorMessage = null
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}