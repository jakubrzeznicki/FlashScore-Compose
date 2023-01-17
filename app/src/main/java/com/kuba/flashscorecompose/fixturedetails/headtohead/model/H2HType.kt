package com.kuba.flashscorecompose.fixturedetails.headtohead.model

import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 17/01/2023.
 */
sealed class H2HType(val titleTextId: Int, open val name: String) {
    class LastFirstTeam(override val name: String) : H2HType(R.string.last_matches, name)
    class LastSecondTeam(override val name: String) : H2HType(R.string.last_matches, name)
    object HeadToHead : H2HType(R.string.between, "")
}