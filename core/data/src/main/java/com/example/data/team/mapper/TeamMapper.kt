package com.example.data.team.mapper

import com.example.data.player.mapper.toBirth
import com.example.data.player.mapper.toBirthEntity
import com.example.database.teams.model.CoachEntity
import com.example.database.teams.model.TeamEntity
import com.example.database.teams.model.VenueEntity
import com.example.model.player.Birth
import com.example.model.team.Coach
import com.example.model.team.Team
import com.example.model.team.Venue
import com.example.network.model.team.CoachDto
import com.example.network.model.team.TeamDto
import com.example.network.model.team.VenueDto

/**
 * Created by jrzeznicki on 14/03/2023.
 */
fun Team.toTeamEntity(
    leagueIdParam: Int? = null,
    seasonParam: Int? = null,
    countryName: String? = null
): TeamEntity {
    return TeamEntity(
        id = id,
        logo = logo,
        name = name,
        isWinner = isWinner,
        isHome = false,
        code = code,
        founded = founded,
        country = countryName ?: country,
        isNational = false,
        leagueId = leagueIdParam ?: leagueId,
        season = seasonParam ?: season
    )
}

fun Venue.toVenueEntity(): VenueEntity {
    return VenueEntity(
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

fun Venue.toVenueEntity(teamId: Int?): VenueEntity {
    return VenueEntity(
        city = city,
        id = id,
        name = name,
        address = address,
        capacity = capacity,
        surface = surface,
        image = image,
        teamId = teamId ?: 0
    )
}

fun Coach.toCoachEntity(): CoachEntity {
    return CoachEntity(
        id = id,
        teamId = teamId,
        name = name,
        photo = photo,
        firstName = firstname,
        lastName = lastname,
        age = age,
        nationality = nationality,
        height = height,
        weight = weight,
        birth = birth.toBirthEntity()
    )
}

fun TeamDto.toTeam(
    leagueIdParam: Int? = null,
    seasonParam: Int? = null,
    countryParam: String? = null
): Team {
    return Team(
        id = id ?: 0,
        logo = logo.orEmpty(),
        name = name.orEmpty(),
        isWinner = winner ?: false,
        code = code.orEmpty(),
        founded = founded ?: 0,
        country = countryParam ?: country.orEmpty(),
        isNational = national ?: false,
        leagueId = leagueIdParam ?: 0,
        season = seasonParam ?: 2022
    )
}

fun VenueDto.toVenue(teamId: Int?): Venue {
    return Venue(
        city = city.orEmpty(),
        id = id ?: 0,
        name = name.orEmpty(),
        address = address.orEmpty(),
        capacity = capacity ?: 0,
        surface = surface.orEmpty(),
        image = image.orEmpty(),
        teamId = teamId ?: 0
    )
}

fun CoachDto.toCoach(teamId: Int): Coach {
    return Coach(
        id = id ?: 0,
        name = name.orEmpty(),
        photo = photo.orEmpty(),
        firstname = firstname.orEmpty(),
        lastname = lastname.orEmpty(),
        age = age ?: 0,
        nationality = nationality.orEmpty(),
        height = height.orEmpty(),
        weight = weight.orEmpty(),
        birth = birth?.toBirth() ?: Birth.EMPTY_BIRTH,
        teamId = teamId
    )
}
