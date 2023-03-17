package com.example.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.RepositoryResult
import com.example.data.player.repository.PlayersDataSource
import com.example.data.team.repository.TeamDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.model.player.Player
import com.example.model.team.Team
import com.example.model.userpreferences.UserPreferences
import com.example.onboarding.model.OnBoardingError
import com.example.onboarding.model.OnBoardingQuestion
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 07/02/2023.
 */
class OnBoardingViewModel(
    private val teamRepository: TeamDataSource,
    private val playersRepository: PlayersDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    private val viewModelState = MutableStateFlow(OnBoardingViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observeTeams()
        viewModelState.value.teams.forEach {
            observePlayers(it.id, SEASON)
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
            it.copy(selectedTeams = selectedTeams)
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
        viewModelState.update { it.copy(selectedPlayers = selectedPlayers) }
        changeQuestion()
    }

    fun onPreviousPressed() {
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

    private fun getIsNextEnabled(questionIndex: Int): Boolean = when {
        viewModelState.value.selectedTeams.isEmpty() && questionIndex == 0 -> false
        viewModelState.value.selectedPlayers.isEmpty() && questionIndex == 1 -> false
        else -> true
    }

    private fun observePlayers(teamId: Int, season: Int) {
        viewModelScope.launch {
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
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            isLoading = false,
                            error = OnBoardingError.RemoteError(result.error)
                        )
                    }
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
            val result = teamRepository.loadTeamsByCountry(COUNTRY)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            isLoading = false,
                            error = OnBoardingError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    fun setOnBoardingAsCompleted() {
        viewModelScope.launch {
            val userId = userPreferencesRepository.getCurrentUserId()
            val favoriteTeamIds = viewModelState.value.selectedTeams.map { it.id }
            val favoritePlayerIds = viewModelState.value.selectedPlayers.map { it.id }
            val userPreferences = UserPreferences(
                userId = userId,
                isOnBoardingCompleted = true,
                favoriteTeamIds = favoriteTeamIds,
                favoritePlayerIds = favoritePlayerIds,
                favoriteFixtureIds = emptyList()
            )
            userPreferencesRepository.saveUserPreferences(userPreferences)
        }
    }

    private companion object {
        const val SEASON = 2022
        const val COUNTRY = "Poland"
    }
}