package com.example.database.userpreferences.mapper

import com.example.database.userpreferences.model.UserPreferencesEntity
import com.example.model.userpreferences.UserPreferences

/**
 * Created by jrzeznicki on 13/03/2023.
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