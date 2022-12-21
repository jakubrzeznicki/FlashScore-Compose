package com.kuba.flashscorecompose.data.country.mapper

import com.kuba.flashscorecompose.data.country.local.model.CountryEntity
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.country.remote.model.CountryDto

/**
 * Created by jrzeznicki on 21/12/2022.
 */
fun CountryEntity.toCountry(): Country {
    return Country(code = code, name = name, flag = flag)
}

fun Country.toCountryEntity(): CountryEntity {
    return CountryEntity(code = code, name = name, flag = flag)
}

fun CountryDto.toCountry(): Country {
    return Country(code = code.orEmpty(), name = name.orEmpty(), flag = flag.orEmpty())
}