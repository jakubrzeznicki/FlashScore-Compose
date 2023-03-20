package com.kuba.flashscorecompose.teamdetails.players.model

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.players.model.Player

/**
 * Created by jrzeznicki on 29/01/2023.
 */
data class PlayerWrapper(val player: Player, val country: Country, val isFavorite: Boolean)