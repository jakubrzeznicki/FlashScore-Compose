package com.kuba.flashscorecompose.onboarding.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.data.players.model.Player
import com.kuba.flashscorecompose.data.team.information.TeamDataSource
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.user.UserDataSource
import com.kuba.flashscorecompose.onboarding.model.OnBoardingError
import com.kuba.flashscorecompose.onboarding.model.OnBoardingQuestion
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 07/02/2023.
 */
class OnBoardingViewModel(
    private val teamRepository: TeamDataSource,
    private val playersRepository: PlayersDataSource,
    private val userRepository: UserDataSource
) : ViewModel() {
    private val viewModelState = MutableStateFlow(OnBoardingViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observeTeams()
        viewModelState.value.teams.forEach {
            Log.d("TEST_LOG", "observe players for tesm - $it")
            observePlayers(it.id, SEASON)
            // observePlayers(team = it, season = SEASON)
        }
    }

    fun refresh() {
        refreshTeams()
    }

    fun teamClicked(team: Team) {
        viewModelState.update {
            val selectedTeams = viewModelState.value.selectedTeams.toMutableList()
            if (selectedTeams.contains(team)) {
                selectedTeams.remove(team)
            } else {
                selectedTeams.add(team)
            }
            it.copy(
                selectedTeams = selectedTeams,
            )
        }
        changeQuestion()
    }

    fun playerClicked(player: Player) {
        val selectedPlayers = viewModelState.value.selectedPlayers.toMutableList()
        if (selectedPlayers.contains(player)) {
            selectedPlayers.remove(player)
        } else {
            selectedPlayers.add(player)
        }
        viewModelState.update {
            it.copy(selectedPlayers = selectedPlayers)
        }
        changeQuestion()
    }

    fun onPreviousPressed() {
        if (viewModelState.value.onBoardingQuestionsData.questionIndex == 0) {
            throw IllegalStateException("onPreviousPressed when on question 0")
        }
        changeQuestion(viewModelState.value.onBoardingQuestionsData.questionIndex - 1)
    }

    fun onNextPressed() {
        changeQuestion(viewModelState.value.onBoardingQuestionsData.questionIndex + 1)
        viewModelState.value.selectedTeams.forEach {
            refreshPlayers(team = it, season = SEASON)
            observePlayers(teamId = it.id, season = SEASON)
        }
    }

    private fun changeQuestion(newQuestionIndex: Int = viewModelState.value.onBoardingQuestionsData.questionIndex) {
        viewModelState.update {
            it.copy(
                onBoardingQuestionsData = it.onBoardingQuestionsData.copy(
                    questionIndex = newQuestionIndex,
                    isNextEnabledButton = getIsNextEnabled(newQuestionIndex),
                    shouldShowPreviousButton = newQuestionIndex == 1,
                    shouldShowDoneButton = newQuestionIndex == 1,
                    onBoardingQuestion = if (newQuestionIndex == 0) {
                        OnBoardingQuestion.Teams
                    } else {
                        OnBoardingQuestion.Players
                    }
                )
            )
        }
    }

    private fun getIsNextEnabled(questionIndex: Int): Boolean =
        when {
            viewModelState.value.selectedTeams.isEmpty() && questionIndex == 0 -> false
            viewModelState.value.selectedPlayers.isEmpty() && questionIndex == 1 -> false
            else -> true
        }

    private fun observePlayers(teamId: Int, season: Int) {
        viewModelScope.launch {
            Log.d("TEST_LOG", "Pbserve opayers beforeeee")
            playersRepository.observePlayers(teamId, season).collect { players ->
                viewModelState.update {
                    it.copy(players = players)
                }
            }
        }
    }

    private fun refreshPlayers(team: Team, season: Int) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = playersRepository.loadPlayers(team, season)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = OnBoardingError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    private fun observeTeams() {
        viewModelScope.launch {
            teamRepository.observeTeams().collect { teams ->
                viewModelState.update {
                    it.copy(teams = teams)
                }
            }
        }
    }

    private fun refreshTeams() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = teamRepository.loadTeamsByCountry("Poland")
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = OnBoardingError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    fun setOnBoardingAsCompleted() {
        viewModelScope.launch {
            userRepository.saveIsOnBoardingCompleted(true)
            Log.d("TEST_LOG", "setOnBoardingAsCompleted")
        }
    }
    fun cleanError() {
        viewModelState.update { it.copy(error = OnBoardingError.NoError) }
    }

    private companion object {
        const val SEASON = 2022
    }
}