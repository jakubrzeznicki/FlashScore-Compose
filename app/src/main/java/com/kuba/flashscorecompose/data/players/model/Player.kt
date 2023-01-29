package com.kuba.flashscorecompose.data.players.model

import com.kuba.flashscorecompose.data.players.model.Birth

data class Player(
    val grid: String,
    val teamId: Int,
    val id: Int,
    val name: String,
    val number: Int,
    val pos: String,
    val firstname: String,
    val lastname: String,
    val age: Int,
    val position: String,
    val birth: Birth,
    val nationality: String,
    val height: String,
    val weight: String,
    val injured: Boolean,
    val photo: String
)