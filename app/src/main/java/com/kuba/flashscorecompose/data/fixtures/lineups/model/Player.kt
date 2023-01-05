package com.kuba.flashscorecompose.data.fixtures.lineups.model

import com.google.gson.annotations.SerializedName

data class Player(
    val grid: String,
    val id: Int,
    val name: String,
    val number: Int,
    val pos: String
)