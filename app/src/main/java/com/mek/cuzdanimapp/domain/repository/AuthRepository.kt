package com.mek.cuzdanimapp.domain.repository

import com.mek.cuzdanimapp.domain.model.AuthResult
import com.mek.cuzdanimapp.domain.model.CurrencyType
import com.mek.cuzdanimapp.util.Resource

interface AuthRepository {
    suspend fun login(email : String, password : String) : Resource<AuthResult>
    suspend fun register(fullName: String, email: String, password: String, currency: CurrencyType?) : Resource<AuthResult>
    suspend fun refresh(refreshToken: String): Resource<AuthResult>
}