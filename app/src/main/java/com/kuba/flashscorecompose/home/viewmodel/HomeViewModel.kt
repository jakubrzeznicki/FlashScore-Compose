package com.kuba.flashscorecompose.home.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.home.model.HomeError
import com.kuba.flashscorecompose.home.model.LeagueFixturesData
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager.showSnackbarMessage
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.containsQuery
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by jrzeznicki on 05/01/2023.
 */
class HomeViewModel(
    private val countryRepository: CountryDataSource,
    private val fixturesRepository: FixturesDataSource,
    private val localDate: LocalDate
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
//        refreshCountries()
//        refreshFixtures()//
        observeCountries()
        observeFixtures()
    }

    fun refresh() {
        refreshCountries()
        refreshFixtures()
    }

    private fun observeCountries() {
        viewModelScope.launch {
            countryRepository.observeCountries(COUNTRY_NAMES).collect { countries ->
                viewModelState.update {
                    it.copy(countries = countries)
                }
            }
        }
    }

    private fun refreshCountries() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = countryRepository.loadCountries()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> {
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
                        it.copy(
                            isLoading = false,
                            error = HomeError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    private fun observeFixtures() {
        viewModelScope.launch {
            val formattedDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
            } else {
                TESTING_DATE
            }
            fixturesRepository.observeFixturesByDate(formattedDate, COUNTRY_NAMES)
                .collect { fixtures ->
                    val leagueFixturesList = fixtures.toLeagueFixturesData()
                    val filteredLeagueFixturesList = filterFixtureItems(leagueFixturesList)
                    viewModelState.update {
                        it.copy(
                            leagueFixturesDataList = leagueFixturesList,
                            filteredLeagueFixtureDataList = filteredLeagueFixturesList,
                            date = TESTING_DATE
                        )
                    }
                }
        }
    }

    private fun List<FixtureItem>.toLeagueFixturesData(): List<LeagueFixturesData> {
        val leagueWithFixtures = groupBy { it.league.id }
        return leagueWithFixtures.map { (leagueId, fixtures) ->
            LeagueFixturesData(
                league = fixtures.map { it.league }.first { it.id == leagueId },
                fixtures = fixtures
            )
        }
    }

    private fun filterFixtureItems(
        leagueFixturesDataList: List<LeagueFixturesData> = viewModelState.value.leagueFixturesDataList,
        selectedCountry: Country = viewModelState.value.selectedCountry
    ): List<LeagueFixturesData> {
        val countryName = selectedCountry.name
        val countryCode = selectedCountry.code
        return leagueFixturesDataList.filter {
            it.league.countryCode.containsQuery(countryCode)
                    || it.league.countryName.containsQuery(countryName)
        }
    }

    fun updateSelectedCountry(newSelectedCountry: Country, isSelected: Boolean) {
        val filteredLeagueFixturesList = if (isSelected) {
            viewModelState.value.leagueFixturesDataList
        } else {
            filterFixtureItems(selectedCountry = newSelectedCountry)
        }
        viewModelState.update {
            it.copy(
                filteredLeagueFixtureDataList = filteredLeagueFixturesList,
                selectedCountry = if (isSelected) Country.EMPTY_COUNTRY else newSelectedCountry
            )
        }
    }

    private fun refreshFixtures() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val formattedDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
            } else {
                TESTING_DATE
            }
            val result = fixturesRepository.loadFixturesByDate(formattedDate)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> {
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
                        it.copy(
                            isLoading = false,
                            error = HomeError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    companion object {
        val COUNTRY_NAMES = listOf(
            "Poland",
            "Germany",
            "France",
            "Spain",
            "Netherlands",
            "Portugal",
            "Turkey",
            "Italy",
            "England"
        )
        const val DATE_FORMAT = "yyy-MM-dd"
        const val TESTING_DATE = "2023-01-22"
    }
}