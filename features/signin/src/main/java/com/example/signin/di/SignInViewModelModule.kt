package com.example.signin.di

import com.example.data.navigation.SignUpBackStackType
import com.example.signin.signin.viewmodel.SignInViewModel
import com.example.signin.singup.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 15/03/2023.
 */
val signInViewModelModule = module {
    viewModel { SignInViewModel(get(), get(), get(), get()) }
    viewModel { (signUpBackStackType: SignUpBackStackType) ->
        SignUpViewModel(
            signUpBackStackType,
            get(),
            get()
        )
    }
}