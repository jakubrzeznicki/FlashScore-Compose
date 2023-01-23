package com.kuba.flashscorecompose.fixturedetails.headtohead.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.HeadToHeadError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

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
                    viewModelState.update { it.copy(h2hFixtures = fixtures.updateYear()) }
                }
        }
    }

    private fun observeHomeTeam() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesByTeam(homeTeamId, season)
                .collect { fixtures ->
                    viewModelState.update { it.copy(homeTeamFixtures = fixtures.updateYear()) }
                }
        }
    }

    private fun observeAwayTeam() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesByTeam(awayTeamId, season)
                .collect { fixtures ->
                    viewModelState.update {
                        it.copy(awayTeamFixtures = fixtures.updateYear())
                    }
                }
        }
    }

    private fun loadHeadToHead() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixturesHeadToHead(
                TEAM_TO_TEAM(homeTeamId, awayTeamId),
                COUNT
            )
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = HeadToHeadError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    private fun loadFixturesByTeam(teamId: Int) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixturesByTeam(teamId, season, COUNT)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = HeadToHeadError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    private fun List<FixtureItem>.updateYear(): List<FixtureItem> {
        return map { fixtureItem ->
            val formattedDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(fixtureItem.fixture.timestamp.toLong()),
                TimeZone.getDefault().toZoneId()
            )
            val year = formattedDate.year.toString()
            fixtureItem.copy(fixture = fixtureItem.fixture.copy(year = year))
        }
    }

    private companion object {
        const val COUNT = 5
        val TEAM_TO_TEAM: (Int, Int) -> String =
            { homeTeamId, awayTeamId -> "$homeTeamId-$awayTeamId" }
    }
}