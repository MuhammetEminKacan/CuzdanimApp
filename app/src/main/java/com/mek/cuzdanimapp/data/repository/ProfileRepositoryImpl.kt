package com.mek.cuzdanimapp.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mek.cuzdanimapp.data.mapper.toDomain
import com.mek.cuzdanimapp.data.remote.ProfileApi
import com.mek.cuzdanimapp.data.remote.dto.profile.ChangePasswordRequestDto
import com.mek.cuzdanimapp.data.remote.dto.profile.DeleteAccountRequestDto
import com.mek.cuzdanimapp.data.remote.dto.profile.UpdateProfileRequestDto
import com.mek.cuzdanimapp.domain.model.User
import com.mek.cuzdanimapp.domain.repository.ProfileRepository
import com.mek.cuzdanimapp.util.Resource
import retrofit2.HttpException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi
) : ProfileRepository {

    override suspend fun getProfile(): Resource<User> {
        return try {
            Resource.Success(api.getProfile().toDomain())
        } catch (e: HttpException) {
            Resource.Error(parseError(e))
        } catch (e: Exception) {
            Resource.Error("CONNECTION_ERROR")
        }
    }

    override suspend fun updateProfile(fullName: String, currency: String): Resource<User> {
        return try {
            Resource.Success(api.updateProfile(UpdateProfileRequestDto(fullName, currency)).toDomain())
        } catch (e: HttpException) {
            Resource.Error(parseError(e))
        } catch (e: Exception) {
            Resource.Error("CONNECTION_ERROR")
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
        } catch (e: HttpException) {
            Resource.Error(parseError(e))
        } catch (e: Exception) {
            Resource.Error("CONNECTION_ERROR")
        }
    }

    override suspend fun deleteAccount(password: String): Resource<Unit> {
        return try {
            api.deleteAccount(DeleteAccountRequestDto(password))
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error(parseError(e))
        } catch (e: Exception) {
            Resource.Error("CONNECTION_ERROR")
        }
    }

    private fun parseError(e: HttpException): String {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val json = Gson().fromJson(errorBody, JsonObject::class.java)
                val code = json.getAsJsonObject("errorDetails")
                    ?.get("code")?.asString ?: "UNKNOWN"
                code
            } else {
                "UNKNOWN"
            }
        } catch (ex: Exception) {
            "UNKNOWN"
        }
    }
}