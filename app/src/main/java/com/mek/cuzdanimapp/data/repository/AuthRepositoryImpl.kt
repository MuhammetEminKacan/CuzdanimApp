package com.mek.cuzdanimapp.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
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
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<AuthResult> {
        return try {
            val response = api.login(LoginRequest(email, password))
            val result = response.toDomain()
            tokenManager.saveTokens(result.accessToken!!, result.refreshToken!!)
            Resource.Success(result)
        } catch (e: HttpException) {
            val (code, _) = parseError(e)
            Resource.Error(code)
        } catch (e: Exception) {
            Resource.Error("CONNECTION_ERROR")
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
            Resource.Success(response.toDomain())
        } catch (e: HttpException) {
            val (code, _) = parseError(e)
            Resource.Error(code)
        } catch (e: Exception) {
            Resource.Error("CONNECTION_ERROR")
        }
    }

    override suspend fun refresh(refreshToken: String): Resource<AuthResult> {
        return try {
            val response = api.refreshToken(RefreshTokenRequest(refreshToken))
            val result = response.toDomain()
            tokenManager.saveTokens(result.accessToken!!, result.refreshToken!!)
            Resource.Success(result)
        } catch (e: HttpException) {
            val (code, _) = parseError(e)
            Resource.Error(code)
        } catch (e: Exception) {
            Resource.Error("CONNECTION_ERROR")
        }
    }

    private fun parseError(e: HttpException): Pair<String, String> {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val json = Gson().fromJson(errorBody, JsonObject::class.java)
                val code = json.getAsJsonObject("errorDetails")
                    ?.get("code")?.asString ?: "UNKNOWN"
                val message = json.getAsJsonObject("errorDetails")
                    ?.get("message")?.asString ?: ""
                Pair(code, message)
            } else {
                Pair("UNKNOWN", e.message())
            }
        } catch (ex: Exception) {
            Pair("UNKNOWN", e.message())
        }
    }
}