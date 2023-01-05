package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.ColumnInfo

data class GoalsEntity(
    @ColumnInfo(name = "home_goal") val home: Int,
    @ColumnInfo(name = "away_goal") val away: Int
)