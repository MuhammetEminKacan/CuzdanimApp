package com.mek.cuzdanimapp.data.remote.dto.auth

data class AuthResponse(
    val accessToken : String,
    val refreshToken : String,
    val tokenType : String,
    val user : UserResponse
)
