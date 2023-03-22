package com.example.explore.di

import com.example.explore.viewmodel.ExploreViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 15/03/2023.
 */

val exploreViewModelModule = module {
    viewModel { ExploreViewModel(get(), get(), get(), get(), get(), get(), get()) }
}