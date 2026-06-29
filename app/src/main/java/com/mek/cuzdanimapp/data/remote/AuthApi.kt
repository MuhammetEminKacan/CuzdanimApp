package com.mek.cuzdanimapp.data.remote

import com.mek.cuzdanimapp.data.remote.dto.auth.AuthResponse
import com.mek.cuzdanimapp.data.remote.dto.auth.LoginRequest
import com.mek.cuzdanimapp.data.remote.dto.auth.RefreshTokenRequest
import com.mek.cuzdanimapp.data.remote.dto.auth.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ) : AuthResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ) : AuthResponse

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ) : AuthResponse

    @POST("auth/resend-verification")
    suspend fun resendVerification(@Query("email") email: String)
}