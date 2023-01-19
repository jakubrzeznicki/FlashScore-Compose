package com.kuba.flashscorecompose.data.standings.local.model

import com.google.gson.annotations.SerializedName

data class InformationStandingEntity(
    val draw: Int,
    val goals: GoalsStandingEntity,
    val lose: Int,
    val played: Int,
    val win: Int
)