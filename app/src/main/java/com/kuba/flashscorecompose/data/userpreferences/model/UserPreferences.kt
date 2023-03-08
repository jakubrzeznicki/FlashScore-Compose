package com.kuba.flashscorecompose.data.userpreferences.model

/**
 * Created by jrzeznicki on 10/02/2023.
 */
data class UserPreferences(
    val userId: String,
    val isOnBoardingCompleted: Boolean,
    val favoriteTeamIds: List<Int>,
    val favoritePlayerIds: List<Int>,
    val favoriteFixtureIds: List<Int>
)
