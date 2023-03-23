package com.example.data.country.mapper

import com.example.database.countries.model.CountryEntity
import com.example.model.country.Country
import com.example.network.model.country.CountryDto

/**
 * Created by jrzeznicki on 14/03/2023.
 */
fun Country.toCountryEntity(): CountryEntity {
    return CountryEntity(code = code, name = name, flag = flag)
}

fun CountryDto.toCountry(): Country {
    return Country(code = code.orEmpty(), name = name.orEmpty(), flag = flag.orEmpty())
}
