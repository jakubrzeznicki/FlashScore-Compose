package com.kuba.flashscorecompose.data.standings.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeam
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeamEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.data.league.mapper.toLeague
import com.kuba.flashscorecompose.data.league.mapper.toLeagueEntity
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.local.model.GoalsStandingEntity
import com.kuba.flashscorecompose.data.standings.local.model.InformationStandingEntity
import com.kuba.flashscorecompose.data.standings.local.model.StandingItemEntity
import com.kuba.flashscorecompose.data.standings.local.model.StandingsEntity
import com.kuba.flashscorecompose.data.standings.model.GoalsStanding
import com.kuba.flashscorecompose.data.standings.model.InformationStanding
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.data.standings.model.Standings
import com.kuba.flashscorecompose.data.standings.remote.model.GoalsStandingDto
import com.kuba.flashscorecompose.data.standings.remote.model.InformationStandingDto
import com.kuba.flashscorecompose.data.standings.remote.model.StandingItemDto
import com.kuba.flashscorecompose.data.standings.remote.model.StandingsDto

/**
 * Created by jrzeznicki on 18/01/2023.
 */

fun Standings.toStandingsEntity(): StandingsEntity {
    return StandingsEntity(
        league = league.toLeagueEntity(),
        leagueId = leagueId,
        season = season,
        standings = standings.map { it.toStandingItemEntity(leagueId) }
    )
}

fun StandingItem.toStandingItemEntity(leagueId: Int): StandingItemEntity {
    return StandingItemEntity(
        all = all.toInformationStandingEntity(),
        away = away.toInformationStandingEntity(),
        home = home.toInformationStandingEntity(),
        description = description,
        form = form,
        goalsDiff = goalsDiff,
        group = group,
        points = points,
        rank = rank,
        status = status,
        team = team.toTeamEntity(leagueId),
        update = update
    )
}

fun InformationStanding.toInformationStandingEntity(): InformationStandingEntity {
    return InformationStandingEntity(
        draw = draw,
        goals = goals.toGoalsStandingEntity(),
        lose = lose,
        played = played,
        win = win
    )
}

fun GoalsStanding.toGoalsStandingEntity(): GoalsStandingEntity {
    return GoalsStandingEntity(against = against, forValue = forValue)
}

fun StandingsEntity.toStandings(): Standings {
    return Standings(
        league = league.toLeague(),
        leagueId = leagueId,
        season = season,
        standings = standings.map { it.toStandingItem() }
    )
}

fun StandingItemEntity.toStandingItem(): StandingItem {
    return StandingItem(
        all = all.toInformationStanding(),
        away = away.toInformationStanding(),
        home = home.toInformationStanding(),
        description = description,
        form = form,
        goalsDiff = goalsDiff,
        group = group,
        points = points,
        rank = rank,
        status = status,
        team = team.toTeam(),
        update = update
    )
}

fun InformationStandingEntity.toInformationStanding(): InformationStanding {
    return InformationStanding(
        draw = draw,
        goals = goals.toGoalsStanding(),
        lose = lose,
        played = played,
        win = win
    )
}

fun GoalsStandingEntity.toGoalsStanding(): GoalsStanding {
    return GoalsStanding(against = against, forValue = forValue)
}

fun StandingsDto.toStandings(): Standings {
    return Standings(
        league = League(
            id = id ?: 0,
            name = name.orEmpty(),
            logo = logo.orEmpty(),
            countryName = country.orEmpty(),
            countryFlag = flag.orEmpty(),
            season = season ?: 0,
            type = "",
            countryCode = "",
            round = "",
        ),
        leagueId = id ?: 0,
        season = season ?: 0,
        standings = standings.flatMap { it?.map { it.toStandingItem() }.orEmpty() }
    )
}

fun StandingItemDto.toStandingItem(): StandingItem {
    return StandingItem(
        all = all?.toInformationStanding() ?: InformationStanding.EMPTY_INFORMATION_STANDING,
        away = away?.toInformationStanding() ?: InformationStanding.EMPTY_INFORMATION_STANDING,
        home = home?.toInformationStanding() ?: InformationStanding.EMPTY_INFORMATION_STANDING,
        description = description.orEmpty(),
        form = form.orEmpty(),
        goalsDiff = goalsDiff ?: 0,
        group = group.orEmpty(),
        points = points ?: 0,
        rank = rank ?: 0,
        status = status.orEmpty(),
        team = team?.toTeam() ?: Team.EMPTY_TEAM,
        update = update.orEmpty()
    )
}

fun InformationStandingDto.toInformationStanding(): InformationStanding {
    return InformationStanding(
        draw = draw ?: 0,
        goals = goals?.toGoalsStanding() ?: GoalsStanding.EMPTY_GOALS_STANDING,
        lose = lose ?: 0,
        played = played ?: 0,
        win = win ?: 0
    )
}

fun GoalsStandingDto.toGoalsStanding(): GoalsStanding {
    return GoalsStanding(against = against ?: 0, forValue = forValue ?: 0)
}