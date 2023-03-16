package com.example.profile.di

import com.example.profile.container.viewmodel.ProfileViewModel
import com.example.profile.details.viewmodel.ProfileDetailsViewModel
import com.example.profile.settings.viewmodel.ProfileSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 15/03/2023.
 */
val profileViewModelModule = module {
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { ProfileDetailsViewModel(get(), get(), get(), get()) }
    viewModel { (userId: String) ->
        ProfileSettingsViewModel(
            userId,
            get(),
            get(),
            get(),
            get()
        )
    }
}