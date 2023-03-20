package com.kuba.flashscorecompose.profile.settings.model

/**
 * Created by jrzeznicki on 09/02/2023.
 */
sealed interface ProfileSettingsItem {
    object Password : ProfileSettingsItem
    object SignIn : ProfileSettingsItem
    object SignOut : ProfileSettingsItem
    object DeleteAccount : ProfileSettingsItem
}