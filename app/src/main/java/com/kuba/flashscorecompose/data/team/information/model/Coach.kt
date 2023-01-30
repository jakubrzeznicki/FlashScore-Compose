package com.kuba.flashscorecompose.data.team.information.model

import com.kuba.flashscorecompose.data.players.model.Birth

data class Coach(
    val id: Int,
    val teamId: Int,
    val name: String,
    val photo: String,
    val firstname: String,
    val lastname: String,
    val age: Int,
    val nationality: String,
    val height: String,
    val weight: String,
    val birth: Birth
) {
    companion object {
        val EMPTY_COACH = Coach(0, 0, "", "", "", "", 0, "", "", "", Birth.EMPTY_BIRTH)
    }
}