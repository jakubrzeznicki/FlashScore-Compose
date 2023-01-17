package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.Embedded

data class ColorsEntity(
    @Embedded(prefix = "goalkeeper_") val goalkeeper: PlayerColorEntity,
    @Embedded(prefix = "player_") val player: PlayerColorEntity
)