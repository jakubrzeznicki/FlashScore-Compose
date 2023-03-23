package com.example.home.di

/**
 * Created by jrzeznicki on 15/03/2023.
 */
import com.example.favoritefixtureinteractor.DefaultFavoriteFixtureInteractor
import com.example.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 15/03/2023.
 */
val homeViewModelModule = module {
    viewModel {
        val favoriteFixtureInteractor = DefaultFavoriteFixtureInteractor(get(), get(), get())
        HomeViewModel(get(), get(), get(), get(), favoriteFixtureInteractor, get())
    }}