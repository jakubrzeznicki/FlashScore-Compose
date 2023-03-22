package com.example.favoritefixtureinteractor

import com.example.data.notification.repository.NotificationsDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.model.fixture.FixtureItem
import com.example.model.fixture.FixtureItemWrapper
import com.example.model.notificationdata.NotificationData
import com.example.notificationservice.manager.ReminderManager

/**
 * Created by jrzeznicki on 06/03/2023.
 */
class DefaultFavoriteFixtureInteractor(
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val reminderManager: ReminderManager,
    private val notificationsRepository: NotificationsDataSource
) : FavoriteFixtureInteractor {

    override suspend fun addFixtureToFavorite(fixtureItemWrapper: FixtureItemWrapper) {
        val savedFavoriteFixtureIds = userPreferencesRepository.getUserPreferences()
            ?.favoriteFixtureIds.orEmpty().toMutableList()
        if (fixtureItemWrapper.isFavorite) {
            savedFavoriteFixtureIds.remove(fixtureItemWrapper.fixtureItem.id)
            cancelReminder(fixtureItemWrapper.fixtureItem)
        } else {
            savedFavoriteFixtureIds.add(fixtureItemWrapper.fixtureItem.id)
            setReminder(fixtureItemWrapper.fixtureItem)
        }
        userPreferencesRepository.saveFavoriteFixturesIds(savedFavoriteFixtureIds)
    }

    private suspend fun setReminder(fixtureItem: FixtureItem) {
        val notificationData = fixtureItem.getNotificationData()
        reminderManager.startReminder(notificationData)
        notificationsRepository.saveReminder(notificationData)
    }

    private suspend fun cancelReminder(fixtureItem: FixtureItem) {
        val notificationData = fixtureItem.getNotificationData()
        reminderManager.cancelReminder(notificationData)
        notificationsRepository.deleteReminder(notificationData.id)
    }

    private fun FixtureItem.getNotificationData(): NotificationData {
        return NotificationData(
            id = id,
            round = round,
            formattedDate = fixture.formattedDate,
            homeTeam = homeTeam.name,
            awayTeam = awayTeam.name,
            timestamp = fixture.timestamp * 1000L
        )
    }
}
