package com.example.ui.di

import com.example.ui.snackbar.DefaultSnackbarManager
import com.example.ui.snackbar.SnackbarManager
import org.koin.dsl.module
import java.time.LocalDate

/**
 * Created by jrzeznicki on 16/03/2023.
 */
val uiModule = module {
    single<SnackbarManager> { DefaultSnackbarManager() }
    single<LocalDate> { LocalDate.now() }
}