package com.kuba.flashscorecompose.account.service

/**
 * Created by jrzeznicki on 06/02/2023.
 */

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}