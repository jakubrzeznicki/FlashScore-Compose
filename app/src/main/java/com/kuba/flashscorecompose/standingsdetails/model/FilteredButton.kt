package com.kuba.flashscorecompose.standingsdetails.model

import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 20/01/2023.
 */
sealed class FilteredButton(val textId: Int) {
    object All : FilteredButton(R.string.all)
    object Home : FilteredButton(R.string.home)
    object Away : FilteredButton(R.string.away)
}