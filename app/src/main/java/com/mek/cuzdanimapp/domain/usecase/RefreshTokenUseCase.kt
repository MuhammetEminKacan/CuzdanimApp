package com.mek.cuzdanimapp.domain.usecase

import com.mek.cuzdanimapp.domain.model.AuthResult
import com.mek.cuzdanimapp.domain.repository.AuthRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(refreshToken: String): Resource<AuthResult> {
        return repository.refresh(refreshToken)
    }
}