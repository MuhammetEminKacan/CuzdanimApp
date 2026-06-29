package com.mek.cuzdanimapp.domain.usecase.auth

import com.mek.cuzdanimapp.domain.repository.AuthRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class ResendVerificationUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Resource<Unit> {
        return repository.resendVerification(email)
    }
}