package com.kuba.flashscorecompose.profile.container.viewmodel

import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.profile.container.model.ProfileUiState

/**
 * Created by jrzeznicki on 08/02/2023.
 */
data class ProfileViewModelState(val isLoading: Boolean = false, val user: User = User()) {
    fun toUiState(): ProfileUiState = ProfileUiState(isLoading, user)
}
