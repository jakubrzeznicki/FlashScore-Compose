package com.kuba.flashscorecompose.data.team.information.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toColors
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toColorsEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Colors
import com.kuba.flashscorecompose.data.fixtures.lineups.mapper.toBirth
import com.kuba.flashscorecompose.data.fixtures.lineups.mapper.toBirthEntity
import com.kuba.flashscorecompose.data.players.model.Birth
import com.kuba.flashscorecompose.data.team.information.local.model.CoachEntity
import com.kuba.flashscorecompose.data.team.information.local.model.TeamEntity
import com.kuba.flashscorecompose.data.team.information.local.model.VenueEntity
import com.kuba.flashscorecompose.data.team.information.model.Coach
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.data.team.information.remote.model.CoachDto
import com.kuba.flashscorecompose.data.team.information.remote.model.TeamDto
import com.kuba.flashscorecompose.data.team.information.remote.model.VenueDto

/**
 * Created by jrzeznicki on 27/01/2023.
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
        colors = colors.toColors()
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

fun Team.toTeamEntity(leagueId: Int?): TeamEntity {
    return TeamEntity(
        id = id,
        logo = logo,
        name = name,
        isWinner = isWinner,
        isHome = false,
        code = code,
        founded = founded,
        country = country,
        isNational = false,
        colors = colors.toColorsEntity(),
        leagueId = leagueId ?: 0
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

fun TeamDto.toTeam(): Team {
    return Team(
        id = id ?: 0,
        logo = logo.orEmpty(),
        name = name.orEmpty(),
        isWinner = winner ?: false,
        code = code.orEmpty(),
        founded = founded ?: 0,
        country = country.orEmpty(),
        isNational = national ?: false,
        colors = colors?.toColors() ?: Colors.EMPTY_COLORS
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