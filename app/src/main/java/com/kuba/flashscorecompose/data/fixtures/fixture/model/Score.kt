package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class Score(
    val extratime: Goals,
    val fulltime: Goals,
    val halftime: Goals,
    val penalty: Goals
) {
    companion object {
        val EMPTY_SCORE =
            Score(Goals.EMPTY_GOALS, Goals.EMPTY_GOALS, Goals.EMPTY_GOALS, Goals.EMPTY_GOALS)
    }
}