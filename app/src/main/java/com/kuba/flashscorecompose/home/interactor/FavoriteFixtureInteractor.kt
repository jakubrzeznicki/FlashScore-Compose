package com.kuba.flashscorecompose.home.interactor

import com.kuba.flashscorecompose.home.model.FixtureItemWrapper

/**
 * Created by jrzeznicki on 06/03/2023.
 */
interface FavoriteFixtureInteractor {
    suspend fun addFixtureToFavorite(
        fixtureItemWrapper: FixtureItemWrapper,
        favoriteFixtureItemWrappers: MutableList<FixtureItemWrapper>
    )
}