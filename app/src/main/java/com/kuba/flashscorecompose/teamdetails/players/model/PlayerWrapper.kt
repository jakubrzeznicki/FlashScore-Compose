package com.kuba.flashscorecompose.teamdetails.players.model

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.players.model.Player

/**
 * Created by jrzeznicki on 29/01/2023.
 */
data class PlayerWrapper(val player: Player, val country: Country, val isFavorite: Boolean) {
    companion object {
        fun List<Player>.toPlayerWrappers(
            countries: List<Country>,
            favoritePlayerIds: List<Int>
        ): List<PlayerWrapper> = map {
            PlayerWrapper(
                player = it,
                country = countries.firstOrNull { country -> country.name == it.nationality }
                    ?: Country.EMPTY_COUNTRY,
                isFavorite = favoritePlayerIds.contains(it.id)
            )
        }
    }
}