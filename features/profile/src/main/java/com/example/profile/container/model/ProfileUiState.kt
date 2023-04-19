package com.example.profile.container.model

import com.example.model.user.User


/**
 * Created by jrzeznicki on 08/02/2023.
 */
interface ProfileUiState {
    val isLoading: Boolean
    val error: ProfileError

    data class HasData(
        override val isLoading: Boolean,
        override val error: ProfileError,
        val user: User
    ) : ProfileUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: ProfileError
    ) : ProfileUiState
}
