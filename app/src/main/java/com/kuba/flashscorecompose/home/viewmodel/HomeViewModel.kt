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
class HomeViewModel(
    private val countryRepository: CountryDataSource,
    private val fixturesRepository: FixturesDataSource,
    private val localDate: LocalDate
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState())

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
        // refreshCountries()
        // refreshFixtures()
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
            Log.d("TEST_LOG", "loadCountries")
            val result = countryRepository.loadCountries()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> {
                        Log.d("TEST_LOG", "refreshCountries success size ${result.data?.size}")
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        Log.d(
                            "TEST_LOG",
                            "refreshCountriesTest error size ${result.error.internalStatus}"
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeFixtures() {
        viewModelScope.launch {
            val formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyy-MM-dd"))
            fixturesRepository.observeFixturesByDate(formattedDate, COUNTRY_NAMES)
                .collect { fixtures ->
                    Log.d("TEST_LOG", "observeFixtures size ${fixtures.size}")
                    viewModelState.update { it.copy(fixtureItems = fixtures) }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshFixtures() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyy-MM-dd"))
            Log.d("TEST_LOG", "refreshFixtures")
            val result = fixturesRepository.loadFixturesByDate(formattedDate)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> {
                        Log.d("TEST_LOG", "refreshFixtures success size ${result.data?.size}")
                        it.copy(isLoading = false)
                    }
                    is RepositoryResult.Error -> {
                        Log.d(
                            "TEST_LOG",
                            "refreshFixtures error size ${result.error.internalStatus}"
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

    fun cleanError() {
        viewModelState.update { it.copy(error = HomeError.NoError) }
    }

    companion object {
        val COUNTRY_CODES = listOf("PL", "DE", "FR", "GB", "ES", "IT", "NL", "PT")
        val COUNTRY_NAMES = listOf(
            "Poland",
            "Deutschland",
            "France",
            "England",
            "Great Britain",
            "Spain",
            "Netherlands",
            "Portugal",
            "Belgium",
            "Turkey"
        )
        const val LAST_X_FIXTURES = 60
    }
}