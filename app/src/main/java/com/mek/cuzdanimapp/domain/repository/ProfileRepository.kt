package com.mek.cuzdanimapp.domain.repository

import com.mek.cuzdanimapp.domain.model.User
import com.mek.cuzdanimapp.util.Resource

interface ProfileRepository {
    suspend fun getProfile(): Resource<User>
    suspend fun updateProfile(fullName: String, currency: String): Resource<User>
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Resource<Unit>
    suspend fun deleteAccount(password: String): Resource<Unit>
}