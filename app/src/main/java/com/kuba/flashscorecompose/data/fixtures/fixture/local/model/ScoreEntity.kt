package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class ScoreEntity(
    @Embedded @ColumnInfo(name = "extra_time") val extratime: GoalsEntity,
    @Embedded @ColumnInfo(name = "full_time") val fulltime: GoalsEntity,
    @Embedded @ColumnInfo(name = "half_time") val halftime: GoalsEntity,
    @Embedded @ColumnInfo(name = "penalty") val penalty: GoalsEntity
)