package com.kuba.flashscorecompose.standingsdetails.viewmodel

import com.kuba.flashscorecompose.data.standings.model.Standing
import com.kuba.flashscorecompose.standingsdetails.model.StandingsDetailsUiState

/**
 * Created by jrzeznicki on 19/01/2023.
 */
data class StandingsDetailsViewModelState(
    val standing: Standing = Standing.EMPTY_Standing
) {
    fun toUiState(): StandingsDetailsUiState = StandingsDetailsUiState(standing = standing)
}