package com.example.fixturedetails.headtohead.model

import com.example.model.fixture.FixtureItem

/**
 * Created by jrzeznicki on 03/02/2023.
 */
data class StyledFixtureItem(
    val fixtureItem: FixtureItem,
    val fixtureItemStyle: FixtureItemStyle
)
