package com.example.home.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 05/01/2023.
 */
sealed class HomeError {
    object NoError : HomeError()
    data class RemoteError(val responseStatus: ResponseStatus) : HomeError()
}
