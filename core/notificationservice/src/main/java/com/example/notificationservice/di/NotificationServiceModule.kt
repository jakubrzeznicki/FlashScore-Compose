package com.example.notificationservice.di

import com.example.notificationservice.manager.DefaultReminderManager
import com.example.notificationservice.manager.ReminderManager
import com.example.notificationservice.notification.DefaultFixtureNotification
import com.example.notificationservice.notification.FixtureNotification
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 14/03/2023.
 */
val notificationServiceModule = module {
    single<ReminderManager> { DefaultReminderManager(androidApplication()) }
    single<FixtureNotification> { DefaultFixtureNotification(androidApplication()) }
}