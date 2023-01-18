package com.kuba.flashscorecompose.utils

import com.kuba.flashscorecompose.data.country.remote.model.CountryDataDto

/**
 * Created by jrzeznicki on 9/7/2022
 */
sealed class RepositoryResult<out T : Any> {
    class Success<out T : Any>(val data: T?) : RepositoryResult<T>()
    class Error(val error: ResponseStatus) : RepositoryResult<Nothing>()
}