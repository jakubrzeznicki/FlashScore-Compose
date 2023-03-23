package com.example.notifications.di

import com.example.notifications.viewmodel.NotificationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 15/03/2023.
 */
val notificationsViewModelModule = module {
    viewModel { NotificationsViewModel(get(), get()) }
}