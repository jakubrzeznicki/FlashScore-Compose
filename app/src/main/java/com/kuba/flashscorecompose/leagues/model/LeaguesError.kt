package com.kuba.flashscorecompose.leagues.model

import com.kuba.flashscorecompose.countries.model.CountriesError
import com.kuba.flashscorecompose.utils.ResponseStatus

/**
 * Created by jrzeznicki on 10/1/2022
 */
sealed class LeaguesError {
    object NoError : LeaguesError()
    data class RemoteError(val responseStatus: ResponseStatus) : LeaguesError()
}