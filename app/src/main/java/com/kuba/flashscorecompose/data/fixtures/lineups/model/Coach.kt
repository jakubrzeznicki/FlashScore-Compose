package com.kuba.flashscorecompose.data.fixtures.lineups.model

import com.google.gson.annotations.SerializedName

data class Coach(val id: Int, val teamId: Int, val name: String, val photo: String) {
    companion object {
        val EMPTY_COACH = Coach(0, 0, "", "")
    }
}