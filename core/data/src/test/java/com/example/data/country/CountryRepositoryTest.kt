package com.example.data.country

import com.example.data.country.local.CountryLocalDataSource
import com.example.data.country.repository.CountryDataSource
import com.example.data.country.repository.CountryRepository
import com.example.database.countries.mapper.toCountry
import com.example.database.countries.model.CountryEntity
import com.example.network.CountryRemoteDataSource
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by jrzeznicki on 08/04/2023.
 */
class CountryRepositoryTest {

    private val local: CountryLocalDataSource = mockk()
    private val remote: CountryRemoteDataSource = mockk()
    private lateinit var repository: CountryDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = CountryRepository(local, remote)
    }

    @Test
    fun `getCountry should call local getCountry`() {
        /* Given */
        val countryName = "countryName"
        val country = CountryEntity(code = "CN", name = countryName, flag = "")
        /* When */
        coEvery { local.getCountry(countryName) } returns country
        runBlocking {
            repository.getCountry(countryName)
        }
        /* Then */
        coVerify(exactly = 1) { local.getCountry(countryName) }
    }

    @Test
    fun `getCountries should call local getCountries`() {
        /* Given */
        val countryName = "countryName"
        val country1 = CountryEntity(code = "CN", name = countryName, flag = "")
        val country2 = CountryEntity(code = "CN2", name = countryName, flag = "")
        val countries = listOf(country1, country2)
        /* When */
        coEvery { local.getCountries() } returns countries
        runBlocking {
            repository.getCountries()
        }
        /* Then */
        coVerify(exactly = 1) { local.getCountries() }
    }

    @Test
    fun `saveCountries should call local saveCountries`() {
        /* Given */
        val countryName = "countryName"
        val country1 = CountryEntity(code = "CN", name = countryName, flag = "")
        val country2 = CountryEntity(code = "CN2", name = countryName, flag = "")
        val countryEntities = listOf(country1, country2)
        val countries = listOf(country1.toCountry(), country2.toCountry())
        /* When */
        coEvery { local.saveCountries(countryEntities) }
        runBlocking {
            repository.saveCountries(countries)
        }
        /* Then */
        coVerify(exactly = 1) { local.saveCountries(countryEntities) }
    }

    @Test
    fun `observeCountries should call local observeCountries`() {
        /* Given */
        val countryName = "countryName"
        val countryName2 = "countryName"
        val countryNames = listOf(countryName, countryName2)
        val country1 = CountryEntity(code = "CN", name = countryName, flag = "")
        val country2 = CountryEntity(code = "CN2", name = countryName, flag = "")
        val countries = listOf(country1, country2)
        val countriesFlow = flowOf(countries)
        /* When */
        every { local.observeCountries(countryNames) } returns countriesFlow
        repository.observeCountries(countryNames)
        /* Then */
        coVerify(exactly = 1) { local.observeCountries(countryNames) }
    }
}