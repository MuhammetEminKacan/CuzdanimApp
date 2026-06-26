package com.mek.cuzdanimapp.data.remote.interceptor

import com.mek.cuzdanimapp.data.local.TokenManager
import com.mek.cuzdanimapp.data.remote.AuthApi
import com.mek.cuzdanimapp.data.remote.dto.auth.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    @Named("refreshAuthApi") private val refreshAuthApi: AuthApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getAccessToken()
        val request = chain.request().newBuilder().apply {
            if (token != null) addHeader("Authorization", "Bearer $token")
        }.build()

        val response = chain.proceed(request)

        if (response.code == 401) {
            response.close()

            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken == null) {
                tokenManager.clearTokens()
                return response
            }

            return try {
                val refreshResponse = runBlocking {
                    refreshAuthApi.refreshToken(RefreshTokenRequest(refreshToken))
                }

                tokenManager.saveTokens(
                    refreshResponse.accessToken,
                    refreshResponse.refreshToken
                )

                val newRequest = chain.request().newBuilder()
                    .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                    .build()

                chain.proceed(newRequest)
            } catch (e: Exception) {
                // Refresh başarısız — token'ları temizle, logout event'i tetikle
                tokenManager.clearTokens()
                response
            }
        }

        return response
    }
}