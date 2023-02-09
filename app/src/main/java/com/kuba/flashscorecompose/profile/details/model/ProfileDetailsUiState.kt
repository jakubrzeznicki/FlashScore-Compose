package com.kuba.flashscorecompose.profile.details.model

import com.kuba.flashscorecompose.data.user.model.User


/**
 * Created by jrzeznicki on 08/02/2023.
 */
data class ProfileDetailsUiState(
    val user: User,
    val isNameExpanded: Boolean,
    val isEmailExpanded: Boolean,
    val isPhoneExpanded: Boolean,
    val isAddressExpanded: Boolean,
    val actualField: ProfileItem
)