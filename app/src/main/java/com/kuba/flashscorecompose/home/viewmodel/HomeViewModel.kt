package com.kuba.flashscorecompose.home.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper
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
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val localDate: LocalDate
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
//        refreshCountries()
//        refreshFixtures()
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
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            val userPreferencesFlow =
                userPreferencesRepository.observeUserPreferences(currentUserId)
            val fixturesFlow =
                fixturesRepository.observeFixturesByDate(formattedDate, COUNTRY_NAMES)
            combine(flow = fixturesFlow, flow2 = userPreferencesFlow) { fixtures, userPreferences ->
                val favoriteFixtureIds = userPreferences.favoriteFixtureIds
                val leagueFixturesList = fixtures.toLeagueFixturesData(favoriteFixtureIds)
                val filteredLeagueFixturesList = filterFixtureItems(leagueFixturesList)
                viewModelState.update {
                    it.copy(
                        leagueFixturesDataList = leagueFixturesList,
                        filteredLeagueFixtureDataList = filteredLeagueFixturesList,
                        date = TESTING_DATE
                    )
                }
            }.collect()
        }
    }

    private fun List<FixtureItem>.toLeagueFixturesData(favoriteFixtureIds: List<Int>): List<LeagueFixturesData> {
        val leagueWithFixtures = groupBy { it.league.id }
        return leagueWithFixtures.map { (leagueId, fixtures) ->
            LeagueFixturesData(
                league = fixtures.map { it.league }.first { it.id == leagueId },
                fixtureWrappers = fixtures.map {
                    FixtureItemWrapper(
                        fixtureItem = it,
                        isFavorite = favoriteFixtureIds.contains(it.id)
                    )
                }
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

    fun addFixtureToFavorite(fixtureItemWrapper: FixtureItemWrapper) {
        viewModelScope.launch {
            val favoriteFixtureItemWrappers =
                viewModelState.value.leagueFixturesDataList.map { it.fixtureWrappers }
                    .flatten()
                    .filter { it.isFavorite }
                    .toMutableList()
            if (fixtureItemWrapper.isFavorite) {
                favoriteFixtureItemWrappers.remove(fixtureItemWrapper)
            } else {
                favoriteFixtureItemWrappers.add(fixtureItemWrapper.copy(isFavorite = true))
            }
            userPreferencesRepository.saveFavoriteFixturesIds(
                favoriteFixtureItemWrappers.map { it.fixtureItem.id }
            )
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