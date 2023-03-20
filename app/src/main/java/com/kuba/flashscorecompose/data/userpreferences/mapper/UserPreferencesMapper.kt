package com.kuba.flashscorecompose.data.userpreferences.mapper

import com.kuba.flashscorecompose.data.userpreferences.local.model.UserPreferencesEntity
import com.kuba.flashscorecompose.data.userpreferences.model.UserPreferences

/**
 * Created by jrzeznicki on 10/02/2023.
 */
fun UserPreferencesEntity.toUserPreferences(): UserPreferences {
    return UserPreferences(
        userId = userId,
        isOnBoardingCompleted = isOnBoardingCompleted,
        favoriteTeamIds = favoriteTeamIds,
        favoritePlayerIds = favoritePlayerIds,
        favoriteFixtureIds = favoriteFixtureIds
    )
}

fun UserPreferences.toUserPreferencesEntity(): UserPreferencesEntity {
    return UserPreferencesEntity(
        userId = userId,
        isOnBoardingCompleted = isOnBoardingCompleted,
        favoriteTeamIds = favoriteTeamIds,
        favoritePlayerIds = favoritePlayerIds,
        favoriteFixtureIds = favoriteFixtureIds
    )
}