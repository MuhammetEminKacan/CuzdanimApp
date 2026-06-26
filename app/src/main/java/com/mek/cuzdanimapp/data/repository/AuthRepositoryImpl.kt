package com.mek.cuzdanimapp.data.repository

import com.mek.cuzdanimapp.data.local.TokenManager
import com.mek.cuzdanimapp.data.mapper.toDomain
import com.mek.cuzdanimapp.data.remote.AuthApi
import com.mek.cuzdanimapp.data.remote.dto.auth.LoginRequest
import com.mek.cuzdanimapp.data.remote.dto.auth.RefreshTokenRequest
import com.mek.cuzdanimapp.data.remote.dto.auth.RegisterRequest
import com.mek.cuzdanimapp.domain.model.AuthResult
import com.mek.cuzdanimapp.domain.model.CurrencyType
import com.mek.cuzdanimapp.domain.repository.AuthRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Resource<AuthResult> {
        return try {
            val response = api.login(LoginRequest(email, password))
            val result = response.toDomain()
            tokenManager.saveTokens(result.accessToken, result.refreshToken)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu")
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        currency: CurrencyType?
    ): Resource<AuthResult> {
        return try {
            val response = api.register(RegisterRequest(fullName, email, password, currency))
            val result = response.toDomain()
            tokenManager.saveTokens(result.accessToken, result.refreshToken)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu")
        }
    }

    override suspend fun refresh(refreshToken: String): Resource<AuthResult> {
        return try {
            val response = api.refreshToken(RefreshTokenRequest(refreshToken))
            val result = response.toDomain()
            tokenManager.saveTokens(result.accessToken, result.refreshToken)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bilinmeyen bir hata oluştu")
        }
    }
}