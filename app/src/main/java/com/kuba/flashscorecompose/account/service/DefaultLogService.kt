package com.kuba.flashscorecompose.account.service

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class DefaultLogService : LogService {
    override fun logNonFatalCrash(throwable: Throwable) =
        Firebase.crashlytics.recordException(throwable)
}
