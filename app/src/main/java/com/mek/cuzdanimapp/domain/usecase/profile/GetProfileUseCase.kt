package com.mek.cuzdanimapp.domain.usecase.profile

import com.mek.cuzdanimapp.domain.model.User
import com.mek.cuzdanimapp.domain.repository.ProfileRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Resource<User> = repository.getProfile()
}