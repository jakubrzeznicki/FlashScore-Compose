package com.kuba.flashscorecompose.utils

/**
 * Created by jrzeznicki on 9/7/2022
 */
sealed class RepositoryResult<out T : Any> {
    class Success<out T : Any>(val data: T?) : RepositoryResult<T>()
    class Error(val error: ResponseStatus) : RepositoryResult<Nothing>()
}
