package com.kuba.flashscorecompose.fixturedetails.headtohead.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.HeadToHeadError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 14/01/2023.
 */
class HeadToHeadViewModel(
    private val homeTeamId: Int,
    private val awayTeamId: Int,
    private val season: Int,
    private val fixturesRepository: FixturesDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HeadToHeadViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
//        loadHeadToHead()
//        loadFixturesByTeam(homeTeamId)
//        loadFixturesByTeam(awayTeamId)
        observeFixturesHeadToHead()
        observeHomeTeam()
        observeAwayTeam()
    }

    fun refresh() {
        loadHeadToHead()
        loadFixturesByTeam(homeTeamId)
        loadFixturesByTeam(awayTeamId)
    }

    private fun observeFixturesHeadToHead() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesHeadToHead(TEAM_TO_TEAM(homeTeamId, awayTeamId))
                .collect { fixtures ->
                    Log.d("TEST_LOG", "observeFixturesHeadToHead size ${fixtures.size}")
                    viewModelState.update { it.copy(h2hFixtures = fixtures) }
                }
        }
    }

    private fun observeHomeTeam() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesByTeam(homeTeamId, season)
                .collect { fixtures ->
                    Log.d("TEST_LOG", "observeHomeTeam size ${fixtures.size}")
                    viewModelState.update { it.copy(homeTeamFixtures = fixtures) }
                }
        }
    }

    private fun observeAwayTeam() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesByTeam(awayTeamId, season)
                .collect { fixtures ->
                    Log.d("TEST_LOG", "observeAwayTeam size ${fixtures.size}")
                    viewModelState.update { it.copy(awayTeamFixtures = fixtures) }
                }
        }
    }

    private fun loadHeadToHead() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            Log.d("TEST_LOG", "loadHeadToHead")
            val result =
                fixturesRepository.loadFixturesHeadToHead(
                    TEAM_TO_TEAM(homeTeamId, awayTeamId),
                    COUNT
                )
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> {
                        Log.d("TEST_LOG", "loadHeadToHead success size ${result.data?.size}")
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        Log.d(
                            "TEST_LOG",
                            "loadHeadToHead error size ${result.error.internalStatus}"
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

    private fun loadFixturesByTeam(teamId: Int) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            Log.d("TEST_LOG", "loadFixturesByTeam")
            val result =
                fixturesRepository.loadFixturesByTeam(teamId, season, COUNT)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> {
                        Log.d("TEST_LOG", "loadFixturesByTeam success size ${result.data?.size}")
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        Log.d(
                            "TEST_LOG",
                            "loadFixturesByTeam error size ${result.error.internalStatus}"
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

    private companion object {
        const val COUNT = 5
        val TEAM_TO_TEAM: (Int, Int) -> String =
            { homeTeamId, awayTeamId -> "$homeTeamId-$awayTeamId" }
    }
}