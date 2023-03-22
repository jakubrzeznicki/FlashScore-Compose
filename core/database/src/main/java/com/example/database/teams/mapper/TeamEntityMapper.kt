package com.example.database.teams.mapper

import com.example.database.players.mapper.toBirth
import com.example.database.teams.model.CoachEntity
import com.example.database.teams.model.TeamEntity
import com.example.database.teams.model.VenueEntity
import com.example.model.team.Coach
import com.example.model.team.Team
import com.example.model.team.Venue

/**
 * Created by jrzeznicki on 13/03/2023.
 */
fun TeamEntity.toTeam(): Team {
    return Team(
        id = id,
        logo = logo,
        name = name,
        isWinner = isWinner,
        code = code,
        founded = founded,
        country = country,
        isNational = isNational,
        leagueId = leagueId,
        season = season
    )
}

fun VenueEntity.toVenue(): Venue {
    return Venue(
        city = city,
        id = id,
        name = name,
        address = address,
        capacity = capacity,
        surface = surface,
        image = image,
        teamId = teamId
    )
}

fun CoachEntity.toCoach(): Coach {
    return Coach(
        id = id,
        name = name,
        photo = photo,
        firstname = firstName,
        lastname = lastName,
        age = age,
        nationality = nationality,
        height = height,
        weight = weight,
        birth = birth.toBirth(),
        teamId = teamId
    )
}