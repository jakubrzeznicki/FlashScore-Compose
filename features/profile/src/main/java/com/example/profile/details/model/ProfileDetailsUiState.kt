package com.example.profile.details.model

import com.example.model.user.User
import com.example.profile.container.model.ProfileError


/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface ProfileDetailsUiState {
    val isLoading: Boolean
    val error: ProfileError
    val isNameExpanded: Boolean
    val isEmailExpanded: Boolean
    val isPhoneExpanded: Boolean
    val isAddressExpanded: Boolean

    data class HasData(
        override val isLoading: Boolean,
        override val error: ProfileError,
        override val isNameExpanded: Boolean,
        override val isEmailExpanded: Boolean,
        override val isPhoneExpanded: Boolean,
        override val isAddressExpanded: Boolean,
        val user: User
    ) : ProfileDetailsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: ProfileError,
        override val isNameExpanded: Boolean,
        override val isEmailExpanded: Boolean,
        override val isPhoneExpanded: Boolean,
        override val isAddressExpanded: Boolean
    ) : ProfileDetailsUiState
}