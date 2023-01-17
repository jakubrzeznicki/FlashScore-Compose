package com.kuba.flashscorecompose.leagues.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.league.LeagueDataSource
import com.kuba.flashscorecompose.leagues.model.DayItem
import com.kuba.flashscorecompose.leagues.model.LeaguesError
import com.kuba.flashscorecompose.utils.DateProgression
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by jrzeznicki on 10/3/2022
 */
class LeaguesViewModel(
    private val countryCode: String,
    private val leagueRepository: LeagueDataSource,
    private val localDate: LocalDate
) : ViewModel() {

    private val viewModelState = MutableStateFlow(LeaguesViewModelState())

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        refreshLeagues()
        observeLeagues()
        getDays()
    }

    private fun observeLeagues() {
        viewModelScope.launch {
            leagueRepository.observeLeagues(countryCode).collect { leagues ->
                viewModelState.update { it.copy(leagueItems = leagues) }
            }
        }
    }

    fun refreshLeagues() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = leagueRepository.loadLeagues(countryCode)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = LeaguesError.RemoteError(result.error)
                    )

                }
            }
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = LeaguesError.NoError) }
    }

    private fun getDays() {
        val weekAgo = localDate.minusWeeks(1)
        val nextWeek = localDate.plusWeeks(1)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.")
        val dateProgression = DateProgression(weekAgo, nextWeek)
        var index = 0
        val days = dateProgression.map {
            DayItem(
                index = index++,
                weekDay = it.dayOfWeek.name.take(3),
                formattedDate = it.format(formatter),
                date = it,
                isSelected = it == localDate
            )
        }
        viewModelState.update { it.copy(dayItems = days) }
    }
}