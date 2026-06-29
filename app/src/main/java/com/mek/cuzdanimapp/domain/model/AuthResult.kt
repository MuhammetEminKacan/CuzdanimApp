package com.mek.cuzdanimapp.domain.model

data class AuthResult(
    val accessToken: String?,
    val refreshToken: String?,
    val user: User
)