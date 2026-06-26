package com.mek.cuzdanimapp.data.mapper

import com.mek.cuzdanimapp.data.remote.dto.auth.AuthResponse
import com.mek.cuzdanimapp.data.remote.dto.auth.UserResponse
import com.mek.cuzdanimapp.domain.model.AuthResult
import com.mek.cuzdanimapp.domain.model.User

fun AuthResponse.toDomain() : AuthResult {
    return AuthResult(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toDomain()
    )
}

fun UserResponse.toDomain() : User {
    return User(
        id = id,
        fullName = fullName,
        email = email,
        currency = currency
    )
}