package com.example.fixturedetails.headtohead.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.RepositoryResult
import com.example.data.fixture.repository.FixturesDataSource
import com.example.fixturedetails.headtohead.model.FixtureItemStyle
import com.example.fixturedetails.headtohead.model.HeadToHeadError
import com.example.fixturedetails.headtohead.model.ScoreStyle
import com.example.fixturedetails.headtohead.model.StyledFixtureItem
import com.example.model.fixture.FixtureItem
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 14/01/2023.
 */
class HeadToHeadViewModel(
    private val homeTeamId: Int,
    private val awayTeamId: Int,
    private val season: Int,
    private val fixturesRepository: FixturesDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HeadToHeadViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
//        refreshHeadToHead()
//        refreshFixturesByTeam(homeTeamId)
//        refreshFixturesByTeam(awayTeamId)
        observeFixturesHeadToHead()
        observeHomeTeam()
        observeAwayTeam()
    }

    fun refresh() {
        refreshHeadToHead()
        refreshFixturesByTeam(homeTeamId)
        refreshFixturesByTeam(awayTeamId)
    }

    private fun observeFixturesHeadToHead() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesHeadToHead(TEAM_TO_TEAM(homeTeamId, awayTeamId))
                .collect { fixtures ->
                    val styledFixtureItems = fixtures.mapToStyledFixtureItems()
                    viewModelState.update { it.copy(h2hFixtures = styledFixtureItems) }
                }
        }
    }

    private fun observeHomeTeam() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesByTeam(homeTeamId, season)
                .collect { fixtures ->
                    val styledFixtureItems = fixtures.mapToStyledFixtureItems()
                    viewModelState.update {
                        it.copy(homeTeamFixtures = styledFixtureItems)
                    }
                }
        }
    }

    private fun observeAwayTeam() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesByTeam(awayTeamId, season)
                .collect { fixtures ->
                    val styledFixtureItems = fixtures.mapToStyledFixtureItems()
                    viewModelState.update {
                        it.copy(awayTeamFixtures = styledFixtureItems)
                    }
                }
        }
    }

    private fun refreshHeadToHead() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixturesHeadToHead(
                TEAM_TO_TEAM(homeTeamId, awayTeamId),
                COUNT
            )
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
                            error = HeadToHeadError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    private fun refreshFixturesByTeam(teamId: Int) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixturesByTeam(teamId, season, COUNT)
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
                            error = HeadToHeadError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    private fun List<FixtureItem>.mapToStyledFixtureItems(): List<StyledFixtureItem> {
        return map { fixtureItem ->
            StyledFixtureItem(
                fixtureItem = fixtureItem,
                fixtureItemStyle = when {
                    fixtureItem.goals.home > fixtureItem.goals.away ->
                        FixtureItemStyle(ScoreStyle.Win, ScoreStyle.Lose)
                    fixtureItem.goals.home < fixtureItem.goals.away ->
                        FixtureItemStyle(ScoreStyle.Lose, ScoreStyle.Win)
                    else -> FixtureItemStyle(ScoreStyle.Draw, ScoreStyle.Draw)
                }
            )
        }
    }

    private companion object {
        const val COUNT = 5
        val TEAM_TO_TEAM: (Int, Int) -> String =
            { homeTeamId, awayTeamId -> "$homeTeamId-$awayTeamId" }
    }
}
