package com.kuba.flashscorecompose.standingsdetails.model

import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 20/01/2023.
 */
sealed class StandingFilterButton(val textId: Int) {
    object All : StandingFilterButton(R.string.all)
    object Home : StandingFilterButton(R.string.home)
    object Away : StandingFilterButton(R.string.away)
}