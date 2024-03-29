package com.example.onboarding.model

import com.example.model.player.Player
import com.example.model.team.Team


/**
 * Created by jrzeznicki on 07/02/2023.
 */
sealed interface OnBoardingUiState {
    val isLoading: Boolean
    val error: OnBoardingError
    val onBoardingQuestionsData: OnBoardingQuestionsData

    data class HasAllData(
        override val isLoading: Boolean,
        override val error: OnBoardingError,
        override val onBoardingQuestionsData: OnBoardingQuestionsData,
        val teams: List<Team>,
        val players: List<Player>,
        val selectedTeams: List<Team>,
        val selectedPlayers: List<Player>
    ) : OnBoardingUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: OnBoardingError,
        override val onBoardingQuestionsData: OnBoardingQuestionsData
    ) : OnBoardingUiState
}
