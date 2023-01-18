package com.kuba.flashscorecompose.countries.model

import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 9/9/2022
 */
sealed class CountriesError {
    object NoError : CountriesError()
    data class RemoteError(val responseStatus: ResponseStatus) : CountriesError()
}
