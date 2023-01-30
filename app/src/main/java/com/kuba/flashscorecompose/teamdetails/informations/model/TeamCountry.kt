package com.kuba.flashscorecompose.teamdetails.informations.model

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.team.information.model.Team

/**
 * Created by jrzeznicki on 30/01/2023.
 */
data class TeamCountry(val team: Team, val country: Country) {
    companion object {
        val EMPTY_TEAM_COUNTRY = TeamCountry(Team.EMPTY_TEAM, Country.EMPTY_COUNTRY)
    }
}