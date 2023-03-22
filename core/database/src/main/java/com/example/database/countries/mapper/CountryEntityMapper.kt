package com.example.database.countries.mapper

import com.example.database.countries.model.CountryEntity
import com.example.model.country.Country

/**
 * Created by jrzeznicki on 13/03/2023.
 */
fun CountryEntity.toCountry(): Country {
    return Country(code = code, name = name, flag = flag)
}