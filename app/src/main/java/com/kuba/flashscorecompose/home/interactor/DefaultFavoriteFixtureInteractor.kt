package com.kuba.flashscorecompose.home.interactor

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.notifications.NotificationsDataSource
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper
import com.kuba.flashscorecompose.notifications.model.NotificationData
import com.kuba.flashscorecompose.notifications.ReminderManager

/**
 * Created by jrzeznicki on 06/03/2023.
 */
class DefaultFavoriteFixtureInteractor(
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val reminderManager: ReminderManager,
    private val notificationsRepository: NotificationsDataSource
) : FavoriteFixtureInteractor {

    override suspend fun addFixtureToFavorite(
        fixtureItemWrapper: FixtureItemWrapper,
        favoriteFixtureItemWrappers: MutableList<FixtureItemWrapper>
    ) {
        if (fixtureItemWrapper.isFavorite) {
            favoriteFixtureItemWrappers.remove(fixtureItemWrapper)
            cancelReminder(fixtureItemWrapper.fixtureItem)
        } else {
            favoriteFixtureItemWrappers.add(fixtureItemWrapper.copy(isFavorite = true))
            setReminder(fixtureItemWrapper.fixtureItem)
        }
        userPreferencesRepository.saveFavoriteFixturesIds(
            favoriteFixtureItemWrappers.map { it.fixtureItem.id }
        )
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