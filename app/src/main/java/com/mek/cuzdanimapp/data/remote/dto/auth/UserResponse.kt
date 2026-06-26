package com.mek.cuzdanimapp.data.remote.dto.auth

import com.mek.cuzdanimapp.domain.model.CurrencyType

data class UserResponse(
    val id : Long,
    val fullName : String,
    val email : String,
    val currency : CurrencyType
)
