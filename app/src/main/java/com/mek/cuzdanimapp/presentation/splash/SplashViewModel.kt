package com.mek.cuzdanimapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.data.local.TokenManager
import com.mek.cuzdanimapp.domain.usecase.RefreshTokenUseCase
import com.mek.cuzdanimapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : ViewModel() {

    private val _effect = Channel<SplashEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val refreshToken = tokenManager.getRefreshToken()

            if (refreshToken == null) {
                _effect.send(SplashEffect.NavigateToLogin)
                return@launch
            }

            when (val result = refreshTokenUseCase(refreshToken)) {
                is Resource.Success -> {
                    _effect.send(SplashEffect.NavigateToDashboard)
                }
                is Resource.Error -> {
                    tokenManager.clearTokens()
                    _effect.send(SplashEffect.NavigateToLogin)
                }
                is Resource.Loading -> Unit
            }
        }
    }
}