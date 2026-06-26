package com.mek.cuzdanimapp.domain.model

data class User(
    val id : Long,
    val fullName : String,
    val email : String,
    val currency : CurrencyType
)
