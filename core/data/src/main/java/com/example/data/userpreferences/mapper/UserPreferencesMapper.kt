package com.example.data.userpreferences.mapper

import com.example.database.userpreferences.model.UserPreferencesEntity
import com.example.model.userpreferences.UserPreferences

/**
 * Created by jrzeznicki on 14/03/2023.
 */
fun UserPreferences.toUserPreferencesEntity(): UserPreferencesEntity {
    return UserPreferencesEntity(
        userId = userId,
        isOnBoardingCompleted = isOnBoardingCompleted,
        favoriteTeamIds = favoriteTeamIds,
        favoritePlayerIds = favoritePlayerIds,
        favoriteFixtureIds = favoriteFixtureIds
    )
}