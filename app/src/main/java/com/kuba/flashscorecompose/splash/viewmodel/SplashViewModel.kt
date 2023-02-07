package com.kuba.flashscorecompose.splash.viewmodel

import android.util.Log
import com.kuba.flashscorecompose.account.service.AccountService
import com.kuba.flashscorecompose.account.service.LogService
import com.kuba.flashscorecompose.main.viewmodel.FlashScoreViewModel

/**
 * Created by jrzeznicki on 06/02/2023.
 */
class SplashViewModel(private val accountService: AccountService, logService: LogService) :
    FlashScoreViewModel(logService) {

    fun onAppStart(openHomeScreen: () -> Unit, openWelcomeScreen: () -> Unit) {
        if (accountService.hasUser) {
            Log.d("TEST_LOG", "Has user - true")
            openHomeScreen()
        } else {
            Log.d("TEST_LOG", "Has user - false")
            openWelcomeScreen()
        }
    }
}