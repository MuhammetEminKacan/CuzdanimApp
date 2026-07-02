package com.mek.cuzdanimapp.presentation.profile

import com.mek.cuzdanimapp.domain.model.User

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val fullName: String = "",
    val selectedCurrency: String = "TRY",
    val isEditSheetVisible: Boolean = false,
    val isPasswordSheetVisible: Boolean = false,
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isSheetLoading: Boolean = false,
    val isDeleteSheetVisible: Boolean = false,
    val deletePassword: String = "",
    val isDeletePasswordVisible: Boolean = false,
    val errorMessage: String? = null,
    val sheetErrorCode: String? = null,
    val sheetErrorMessage: String? = null
)