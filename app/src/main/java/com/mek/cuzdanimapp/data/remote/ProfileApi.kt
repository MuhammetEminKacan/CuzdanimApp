package com.mek.cuzdanimapp.data.remote

import com.mek.cuzdanimapp.data.remote.dto.auth.UserResponse
import com.mek.cuzdanimapp.data.remote.dto.profile.ChangePasswordRequestDto
import com.mek.cuzdanimapp.data.remote.dto.profile.DeleteAccountRequestDto
import com.mek.cuzdanimapp.data.remote.dto.profile.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProfileApi {

    @GET("profile/get")
    suspend fun getProfile(): UserResponse

    @PUT("profile/update")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto
    ): UserResponse

    @PUT("profile/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequestDto
    )

    @POST("profile/delete-account")
    suspend fun deleteAccount(@Body request: DeleteAccountRequestDto)
}