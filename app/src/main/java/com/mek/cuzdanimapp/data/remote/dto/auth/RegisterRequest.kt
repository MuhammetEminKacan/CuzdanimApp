package com.mek.cuzdanimapp.data.remote.dto.auth

import com.mek.cuzdanimapp.domain.model.CurrencyType

data class RegisterRequest(
    val fullName : String,
    val email : String,
    val password : String,
    val currency : CurrencyType? = null
)