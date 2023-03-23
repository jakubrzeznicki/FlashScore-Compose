package com.example.welcome.di

import com.example.welcome.splash.viewmodel.SplashViewModel
import com.example.welcome.welcome.viewmodel.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
/**
 * Created by jrzeznicki on 15/03/2023.
 */
val welcomeViewModelModule = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { WelcomeViewModel(get(), get(), get(), get()) }
}