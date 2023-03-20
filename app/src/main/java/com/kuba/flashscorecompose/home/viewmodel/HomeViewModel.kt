package com.kuba.flashscorecompose.home.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.home.interactor.FavoriteFixtureInteractor
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper
import com.kuba.flashscorecompose.home.model.HomeError
import com.kuba.flashscorecompose.home.model.LeagueFixturesData
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
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
    private val snackbarManager: SnackbarManager,
    private val favoriteFixtureInteractor: FavoriteFixtureInteractor,
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
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
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
                val filteredLeagueFixturesList = filterLeagueFixturesData(leagueFixturesList)
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

    private fun filterLeagueFixturesData(
        leagueFixturesDataList: List<LeagueFixturesData> = viewModelState.value.leagueFixturesDataList,
        query: String = viewModelState.value.searchQuery,
        selectedCountry: Country = viewModelState.value.selectedCountry
    ): List<LeagueFixturesData> {
        val countryName = selectedCountry.name
        val countryCode = selectedCountry.code
        val filterFixtureItemWrappers = filterFixtureItemWrappers(leagueFixturesDataList, query)
        return filterFixtureItemWrappers.filter {
            it.league.countryCode.containsQuery(countryCode) ||
                it.league.countryName.containsQuery(countryName)
        }
    }

    private fun filterFixtureItemWrappers(
        leagueFixturesDataList: List<LeagueFixturesData>,
        query: String = viewModelState.value.searchQuery
    ): List<LeagueFixturesData> {
        return leagueFixturesDataList.map {
            val fixtureWrappers = it.fixtureWrappers.filter { fixtureItemWrapper ->
                fixtureItemWrapper.fixtureItem.league.name.containsQuery(query) ||
                    fixtureItemWrapper.fixtureItem.fixture.venue.city.containsQuery(query) ||
                    fixtureItemWrapper.fixtureItem.fixture.venue.name.containsQuery(query) ||
                    fixtureItemWrapper.fixtureItem.homeTeam.name.containsQuery(query) ||
                    fixtureItemWrapper.fixtureItem.awayTeam.name.containsQuery(query)
            }
            LeagueFixturesData(
                league = it.league,
                fixtureWrappers = fixtureWrappers
            )
        }.filter { it.fixtureWrappers.isNotEmpty() }
    }

    fun updateSelectedCountry(newSelectedCountry: Country, isSelected: Boolean) {
        val filteredLeagueFixturesList = if (isSelected) {
            filterFixtureItemWrappers(viewModelState.value.leagueFixturesDataList)
        } else {
            filterLeagueFixturesData(selectedCountry = newSelectedCountry)
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
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
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
            favoriteFixtureInteractor.addFixtureToFavorite(fixtureItemWrapper)
        }
    }

    fun updateSearchQuery(newQuery: String) {
        val filteredFixtures = filterLeagueFixturesData(query = newQuery)
        viewModelState.update {
            it.copy(filteredLeagueFixtureDataList = filteredFixtures, searchQuery = newQuery)
        }
    }

    fun onSearchClick() {
        viewModelState.update { it.copy(isSearchExpanded = !it.isSearchExpanded) }
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
