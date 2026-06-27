package com.mek.cuzdanimapp.domain.usecase.auth

import com.mek.cuzdanimapp.domain.model.AuthResult
import com.mek.cuzdanimapp.domain.repository.AuthRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Resource<AuthResult> {
        return repository.login(email, password)
    }
}