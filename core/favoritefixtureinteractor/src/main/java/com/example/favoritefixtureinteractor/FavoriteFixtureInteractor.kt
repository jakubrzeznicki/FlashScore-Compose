package com.example.favoritefixtureinteractor

import com.example.model.fixture.FixtureItemWrapper

/**
 * Created by jrzeznicki on 06/03/2023.
 */
interface FavoriteFixtureInteractor {
    suspend fun addFixtureToFavorite(fixtureItemWrapper: FixtureItemWrapper)
}
