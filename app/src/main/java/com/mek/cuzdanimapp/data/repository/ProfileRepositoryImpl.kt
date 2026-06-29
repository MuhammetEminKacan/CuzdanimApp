package com.mek.cuzdanimapp.data.repository

import com.mek.cuzdanimapp.data.mapper.toDomain
import com.mek.cuzdanimapp.data.remote.ProfileApi
import com.mek.cuzdanimapp.data.remote.dto.profile.ChangePasswordRequestDto
import com.mek.cuzdanimapp.data.remote.dto.profile.DeleteAccountRequestDto
import com.mek.cuzdanimapp.data.remote.dto.profile.UpdateProfileRequestDto
import com.mek.cuzdanimapp.domain.model.User
import com.mek.cuzdanimapp.domain.repository.ProfileRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi
) : ProfileRepository {

    override suspend fun getProfile(): Resource<User> {
        return try {
            Resource.Success(api.getProfile().toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun updateProfile(
        fullName: String,
        currency: String
    ): Resource<User> {
        return try {
            Resource.Success(
                api.updateProfile(UpdateProfileRequestDto(fullName, currency)).toDomain()
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Resource<Unit> {
        return try {
            api.changePassword(ChangePasswordRequestDto(oldPassword, newPassword, confirmPassword))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun deleteAccount(password: String): Resource<Unit> {
        return try {
            api.deleteAccount(DeleteAccountRequestDto(password))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }
}