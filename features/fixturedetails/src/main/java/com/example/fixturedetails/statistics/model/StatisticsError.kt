package com.example.fixturedetails.statistics.model

import com.example.common.utils.ResponseStatus

/**
 * Created by jrzeznicki on 12/01/2023.
 */
sealed class StatisticsError {
    object NoError : StatisticsError()
    data class RemoteError(val responseStatus: ResponseStatus) : StatisticsError()
}
