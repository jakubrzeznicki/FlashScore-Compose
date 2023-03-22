package com.kuba.flashscorecompose.di

import com.example.authentication.di.authenticationModule
import com.example.data.di.repositoryModule
import com.example.database.di.storageModule
import com.example.datastore.di.dataStoreModule
import com.example.explore.di.exploreViewModelModule
import com.example.fixturedetails.di.fixtureDetailsViewModelModule
import com.example.home.di.homeViewModelModule
import com.example.leaguedetails.di.leagueDetailsViewModelModule
import com.example.network.di.networkModule
import com.example.notifications.di.notificationsViewModelModule
import com.example.notificationservice.di.notificationServiceModule
import com.example.onboarding.di.onBoardingViewModelModule
import com.example.playerdetails.di.playerDetailsViewModelModule
import com.example.profile.di.profileViewModelModule
import com.example.signin.di.signInViewModelModule
import com.example.standings.di.standingsViewModelModule
import com.example.standingsdetails.di.standingsDetailsViewModelModule
import com.example.teamdetails.di.teamDetailsViewModelModule
import com.example.ui.di.uiModule
import com.example.welcome.di.welcomeViewModelModule


/**
 * Created by jrzeznicki on 9/5/2022
 */
class KoinModules {
    fun getAllModules() = listOf(
        authenticationModule,
        repositoryModule,
        storageModule,
        dataStoreModule,
        networkModule,
        notificationServiceModule,
        uiModule,
        exploreViewModelModule,
        fixtureDetailsViewModelModule,
        homeViewModelModule,
        leagueDetailsViewModelModule,
        notificationsViewModelModule,
        onBoardingViewModelModule,
        playerDetailsViewModelModule,
        profileViewModelModule,
        signInViewModelModule,
        standingsViewModelModule,
        standingsDetailsViewModelModule,
        teamDetailsViewModelModule,
        welcomeViewModelModule
    )
}
