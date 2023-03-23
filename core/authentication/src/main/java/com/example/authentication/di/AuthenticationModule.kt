package com.example.authentication.di

import com.example.authentication.repository.AuthenticationDataSource
import com.example.authentication.repository.AuthenticationRepository
import com.example.authentication.service.DefaultLogService
import com.example.authentication.service.LogService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 14/03/2023.
 */
val authenticationModule = module {
    single<AuthenticationDataSource> {
        val auth = Firebase.auth
        val storage = Firebase.storage
        AuthenticationRepository(auth, storage, get())
    }
    single<LogService> {
        DefaultLogService()
    }
}