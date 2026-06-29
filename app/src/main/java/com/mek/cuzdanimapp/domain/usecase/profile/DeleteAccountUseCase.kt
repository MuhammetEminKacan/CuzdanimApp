package com.mek.cuzdanimapp.domain.usecase.profile

import com.mek.cuzdanimapp.domain.repository.ProfileRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(password: String): Resource<Unit> {
        return repository.deleteAccount(password)
    }
}