package com.kuba.flashscorecompose.data.fixtures.fixture.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Score(
    val extratime: Goals,
    val fulltime: Goals,
    val halftime: Goals,
    val penalty: Goals
) : Parcelable{
    companion object {
        val EMPTY_SCORE =
            Score(Goals.EMPTY_GOALS, Goals.EMPTY_GOALS, Goals.EMPTY_GOALS, Goals.EMPTY_GOALS)
    }
}