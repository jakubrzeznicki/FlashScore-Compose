package com.kuba.flashscorecompose.home.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.home.model.HomeError
import com.kuba.flashscorecompose.home.model.LeagueFixturesData
import com.kuba.flashscorecompose.utils.RepositoryResult
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
//        refreshFixtures()
        observeCountries()
        observeFixtures()
    }

    fun refresh() {
//        refreshCountries()
//        refreshFixtures()
    }

    private fun observeCountries() {
        viewModelScope.launch {
            countryRepository.observeCountries(COUNTRY_CODES).collect { countries ->
                viewModelState.update {
                    it.copy(countryItems = countries)
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
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = HomeError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    fun getFixturesByCountry(countryName: String, isSelected: Boolean) {
        viewModelScope.launch {
            val countryNames = if (isSelected) COUNTRY_NAMES else listOf(countryName)
            val fixtures = fixturesRepository.getFixturesByCountry(countryNames)
            val leagueFixturesList = fixtures.toLeagueFixturesData()
            viewModelState.update { it.copy(leagueFixturesDataList = leagueFixturesList) }
        }
    }

    private fun observeFixtures() {
        viewModelScope.launch {
            val formattedDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
            } else {
                "2021-07-14"
            }
            fixturesRepository.observeFixturesByDate(formattedDate, COUNTRY_NAMES)
                .collect { fixtures ->
                    val leagueFixturesList = fixtures.toLeagueFixturesData()
                    viewModelState.update { it.copy(leagueFixturesDataList = leagueFixturesList) }
                }
        }
    }

    private fun List<FixtureItem>.toLeagueFixturesData(): List<LeagueFixturesData> {
        val leagueWithFixtures = groupBy { it.league.id }
        return leagueWithFixtures.map { (leagueId, fixtures) ->
            LeagueFixturesData(
                league = fixtures.map { it.league }.first() { it.id == leagueId },
                fixtures = fixtures
            )
        }
    }

    private fun refreshFixtures() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val formattedDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
            } else {
                "2021-07-14"
            }
            val result = fixturesRepository.loadFixturesByDate(formattedDate)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = HomeError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = HomeError.NoError) }
    }

    companion object {
        val COUNTRY_CODES = listOf("PL", "DE", "FR", "ES", "IT", "NL", "PT", "TR", "UA", "BE", "GB")
        val COUNTRY_NAMES = listOf(
            "Poland",
            "Deutschland",
            "France",
            "Spain",
            "Netherlands",
            "Portugal",
            "Turkey",
            "Ukraine",
            "Belgium",
            "England"
        )
        const val LAST_X_FIXTURES = 60
        const val DATE_FORMAT = "yyy-MM-dd"
    }
}