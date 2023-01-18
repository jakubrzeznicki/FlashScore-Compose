package com.kuba.flashscorecompose.home.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.home.model.HomeError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by jrzeznicki on 05/01/2023.
 */
@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val countryRepository: CountryDataSource,
    private val fixturesRepository: FixturesDataSource,
    private val localDate: LocalDate
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState())
    private val formationDate by lazy { localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) }
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    @RequiresApi(Build.VERSION_CODES.O)
    fun setup() {
        //refreshCountries()
        //refreshFixtures()
        observeCountries()
        observeFixtures()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            val leagueWithFixtures = fixtures.groupBy { it.league }
            viewModelState.update { it.copy(leagueWithFixtures = leagueWithFixtures) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeFixtures() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesByDate(formationDate, COUNTRY_NAMES)
                .collect { fixtures ->
                    val leagueWithFixtures = fixtures.groupBy { it.league }
                    viewModelState.update { it.copy(leagueWithFixtures = leagueWithFixtures) }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshFixtures() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixturesByDate(formationDate)
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