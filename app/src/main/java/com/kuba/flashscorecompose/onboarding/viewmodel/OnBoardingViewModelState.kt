package com.kuba.flashscorecompose.onboarding.viewmodel

import com.kuba.flashscorecompose.data.players.model.Player
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.onboarding.model.OnBoardingError
import com.kuba.flashscorecompose.onboarding.model.OnBoardingQuestionsData
import com.kuba.flashscorecompose.onboarding.model.OnBoardingUiState

/**
 * Created by jrzeznicki on 07/02/2023.
 */
data class OnBoardingViewModelState(
    val isLoading: Boolean = false,
    val error: OnBoardingError = OnBoardingError.NoError,
    val onBoardingQuestionsData: OnBoardingQuestionsData = OnBoardingQuestionsData(),
    val teams: List<Team> = emptyList(),
    val players: List<Player> = emptyList(),
    val selectedTeams: List<Team> = emptyList(),
    val selectedPlayers: List<Player> = emptyList(),

    ) {
    fun toUiState(): OnBoardingUiState = when {
        teams.isNotEmpty() || players.isNotEmpty() ->
            OnBoardingUiState.HasAllData(
                isLoading,
                error,
                onBoardingQuestionsData,
                teams,
                players,
                selectedTeams,
                selectedPlayers
            )
        else -> OnBoardingUiState.NoData(isLoading, error, onBoardingQuestionsData)
    }
}