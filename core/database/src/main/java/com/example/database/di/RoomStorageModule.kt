package com.example.database.di

import com.example.database.LocalRoomStorage
import com.example.database.RoomStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 13/03/2023.
 */

val storageModule = module {
    single<RoomStorage> {
        LocalRoomStorage(androidApplication())
    }
}