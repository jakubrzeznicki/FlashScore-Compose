package com.kuba.flashscorecompose.fixturedetails.lineup.model

/**
 * Created by jrzeznicki on 17/01/2023.
 */
sealed interface TeamTab {
    object Home : TeamTab
    object Away : TeamTab
}