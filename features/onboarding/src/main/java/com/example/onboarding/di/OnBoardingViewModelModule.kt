package com.example.onboarding.di

import com.example.onboarding.viewmodel.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 15/03/2023.
 */
val onBoardingViewModelModule = module {
    viewModel { OnBoardingViewModel(get(), get(), get(), get()) }
}