package com.mek.cuzdanimapp.domain.usecase.profile

import com.mek.cuzdanimapp.domain.repository.ProfileRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Resource<Unit> = repository.changePassword(oldPassword, newPassword, confirmPassword)
}