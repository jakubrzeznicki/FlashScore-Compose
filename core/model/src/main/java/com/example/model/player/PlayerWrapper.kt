package com.example.model.player

import com.example.model.country.Country

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
