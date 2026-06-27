package com.mek.cuzdanimapp.domain.usecase.auth

import com.mek.cuzdanimapp.domain.model.AuthResult
import com.mek.cuzdanimapp.domain.model.CurrencyType
import com.mek.cuzdanimapp.domain.repository.AuthRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        currency: CurrencyType?
    ): Resource<AuthResult> {
        return repository.register(fullName, email, password, currency)
    }
}