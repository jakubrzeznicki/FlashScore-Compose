package com.example.database.userpreferences.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jrzeznicki on 10/02/2023.
 */
@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "is_on_boarding_completed") val isOnBoardingCompleted: Boolean,
    @ColumnInfo(name = "favorite_team_ids") val favoriteTeamIds: List<Int>,
    @ColumnInfo(name = "favorite_player_ids") val favoritePlayerIds: List<Int>,
    @ColumnInfo(name = "favorite_fixture_ids") val favoriteFixtureIds: List<Int>
)
