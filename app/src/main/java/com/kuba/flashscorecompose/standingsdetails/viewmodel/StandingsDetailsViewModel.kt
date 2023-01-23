package com.kuba.flashscorecompose.standingsdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.standings.StandingsDataSource
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.standingsdetails.model.StandingFilterButton
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
        getStandings()
    }

    private fun getStandings() {
        viewModelScope.launch {
            val standing = standingsRepository.getStanding(leagueId, season)
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

    fun filterStandings(newStandingFilterButton: StandingFilterButton) {
        val filteredStandingItems =
            filterStandingItems(standingFilterButton = newStandingFilterButton)
        viewModelState.update {
            it.copy(
                filteredStandings = filteredStandingItems,
                standingFilterButton = newStandingFilterButton
            )
        }
    }

    private fun filterStandingItems(
        standingItems: List<StandingItem> = viewModelState.value.standingsItems,
        standingFilterButton: StandingFilterButton = viewModelState.value.standingFilterButton
    ): List<StandingItem> {
        return when (standingFilterButton) {
            is StandingFilterButton.All -> standingItems.map {
                it.copy(
                    selectedInformationStanding = it.all,
                    colorId = getColorId(it.rank)
                )
            }
            is StandingFilterButton.Home -> standingItems.map {
                it.copy(
                    selectedInformationStanding = it.home,
                    colorId = getColorId(it.rank)
                )
            }
            is StandingFilterButton.Away -> standingItems.map {
                it.copy(
                    selectedInformationStanding = it.away,
                    colorId = getColorId(it.rank)
                )
            }
        }
    }

    private fun getColorId(rank: Int): Int {
        return when (rank) {
            in 1..3 -> R.color.darkBlue
            in 4..5 -> R.color.darkRed
            else -> R.color.black500
        }
    }
}