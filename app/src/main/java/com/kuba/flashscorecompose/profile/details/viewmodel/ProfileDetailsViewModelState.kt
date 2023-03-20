package com.kuba.flashscorecompose.profile.details.viewmodel

import com.kuba.flashscorecompose.data.user.model.User
import com.kuba.flashscorecompose.profile.details.model.ProfileDetailsUiState

/**
 * Created by jrzeznicki on 08/02/2023.
 */
data class ProfileDetailsViewModelState(
    val user: User = User(),
    val isNameExpanded: Boolean = false,
    val isEmailExpanded: Boolean = false,
    val isPhoneExpanded: Boolean = false,
    val isAddressExpanded: Boolean = false
) {
    fun toUiState(): ProfileDetailsUiState = ProfileDetailsUiState(
        user,
        isNameExpanded,
        isEmailExpanded,
        isPhoneExpanded,
        isAddressExpanded
    )
}