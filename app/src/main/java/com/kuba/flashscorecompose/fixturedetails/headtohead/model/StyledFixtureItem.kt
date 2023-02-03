package com.kuba.flashscorecompose.fixturedetails.headtohead.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem

/**
 * Created by jrzeznicki on 03/02/2023.
 */
data class StyledFixtureItem(
    val fixtureItem: FixtureItem,
    val fixtureItemStyle: FixtureItemStyle
)