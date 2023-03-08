package com.kuba.flashscorecompose.explore.model

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.team.information.model.Team

/**
 * Created by jrzeznicki on 02/02/2023.
 */
data class TeamWrapper(val team: Team, val country: Country, val isFavorite: Boolean)
