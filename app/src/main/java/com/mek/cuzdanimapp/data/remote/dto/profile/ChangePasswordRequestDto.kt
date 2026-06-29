package com.mek.cuzdanimapp.data.remote.dto.profile

data class ChangePasswordRequestDto(
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
)