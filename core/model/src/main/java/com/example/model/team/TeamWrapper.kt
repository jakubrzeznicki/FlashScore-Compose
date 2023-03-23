package com.example.model.team

import com.example.model.country.Country

/**
 * Created by jrzeznicki on 02/02/2023.
 */
data class TeamWrapper(val team: Team, val country: Country, val isFavorite: Boolean)
