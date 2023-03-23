package com.example.profile.details.model

import com.example.model.user.User


/**
 * Created by jrzeznicki on 08/02/2023.
 */
data class ProfileDetailsUiState(
    val isLoading: Boolean,
    val user: User,
    val isNameExpanded: Boolean,
    val isEmailExpanded: Boolean,
    val isPhoneExpanded: Boolean,
    val isAddressExpanded: Boolean
)
