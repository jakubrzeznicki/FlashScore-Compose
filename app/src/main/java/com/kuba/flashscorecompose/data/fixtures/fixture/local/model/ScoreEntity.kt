package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.Embedded

data class ScoreEntity(
    @Embedded(prefix = "extra_time_") val extratime: GoalsEntity,
    @Embedded(prefix = "full_time_") val fulltime: GoalsEntity,
    @Embedded(prefix = "half_time_") val halftime: GoalsEntity,
    @Embedded(prefix = "penalty_") val penalty: GoalsEntity
)
