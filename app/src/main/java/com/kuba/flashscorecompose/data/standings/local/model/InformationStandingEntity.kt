package com.kuba.flashscorecompose.data.standings.local.model

data class InformationStandingEntity(
    val draw: Int,
    val goals: GoalsStandingEntity,
    val lose: Int,
    val played: Int,
    val win: Int
)
