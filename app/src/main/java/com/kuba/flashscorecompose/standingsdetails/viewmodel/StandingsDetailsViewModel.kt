package com.kuba.flashscorecompose.standingsdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.standings.StandingsDataSource
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.standingsdetails.model.StandingsDetailsError
import com.kuba.flashscorecompose.ui.component.chips.FilterChip
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 19/01/2023.
 */
class StandingsDetailsViewModel(
    private val leagueId: Int,
    private val season: Int,
    private val standingsRepository: StandingsDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(StandingsDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observeStandings()
    }

    fun refresh() {
        refreshStanding()
    }

    private fun observeStandings() {
        viewModelScope.launch {
            standingsRepository.observeStanding(leagueId, season).collect { standing ->
                if (standing == null) {
                    viewModelState.update { it.copy(error = StandingsDetailsError.EmptyDatabase) }
                    return@collect
                }
                val filteredStandingItems = filterStandingItems(standing.standingItems)
                viewModelState.update {
                    it.copy(
                        league = standing.league,
                        standingsItems = standing.standingItems,
                        filteredStandings = filteredStandingItems
                    )
                }
            }
        }
    }

    fun filterStandings(newStandingFilterChip: FilterChip.Standings) {
        val filteredStandingItems =
            filterStandingItems(standingFilterChip = newStandingFilterChip)
        viewModelState.update {
            it.copy(
                filteredStandings = filteredStandingItems,
                standingFilterChip = newStandingFilterChip
            )
        }
    }

    private fun refreshStanding() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = standingsRepository.loadStandings(leagueId, season)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = StandingsDetailsError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    private fun filterStandingItems(
        standingItems: List<StandingItem> = viewModelState.value.standingsItems,
        standingFilterChip: FilterChip.Standings = viewModelState.value.standingFilterChip
    ): List<StandingItem> {
        return when (standingFilterChip) {
            is FilterChip.Standings.All -> standingItems.map {
                it.copy(
                    selectedInformationStanding = it.all,
                    colorId = getColorId(it.rank),
                    goalsDiff = it.all.goals.forValue - it.all.goals.against,
                    points = calculatePoints(it.all.win, it.all.draw)
                )
            }
            is FilterChip.Standings.Home -> {
                val sortedStandingItems =
                    standingItems.map { it to calculatePoints(it.home.win, it.home.draw) }
                        .sortedBy { it.second }.asReversed()
                sortedStandingItems.mapIndexed { index, standingsWithPoints ->
                    val standingItem = standingsWithPoints.first
                    val points = standingsWithPoints.second
                    standingsWithPoints.first.copy(
                        selectedInformationStanding = standingItem.home,
                        colorId = getColorId(index + ONE_POSITION),
                        goalsDiff = standingItem.home.goals.forValue - standingItem.home.goals.against,
                        points = points,
                        rank = index + ONE_POSITION
                    )
                }
            }
            is FilterChip.Standings.Away -> {
                val sortedStandingItems =
                    standingItems.map { it to calculatePoints(it.away.win, it.away.draw) }
                        .sortedBy { it.second }.asReversed()
                sortedStandingItems.mapIndexed { index, standingsWithPoints ->
                    val standingItem = standingsWithPoints.first
                    val points = standingsWithPoints.second
                    standingsWithPoints.first.copy(
                        selectedInformationStanding = standingItem.away,
                        colorId = getColorId(index + ONE_POSITION),
                        goalsDiff = standingItem.away.goals.forValue - standingItem.away.goals.against,
                        points = points,
                        rank = index + ONE_POSITION
                    )
                }
            }
        }
    }

    private fun calculatePoints(wins: Int, draws: Int): Int =
        wins * WIN_FACTOR + draws * DRAW_FACTOR

    private fun getColorId(rank: Int): Int {
        return when (rank) {
            in 1..3 -> R.color.darkBlue
            in 4..5 -> R.color.darkRed
            else -> R.color.black500
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = StandingsDetailsError.NoError) }
    }

    private companion object {
        const val WIN_FACTOR = 3
        const val DRAW_FACTOR = 1
        const val ONE_POSITION = 1
    }
}