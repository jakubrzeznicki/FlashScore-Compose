package com.kuba.flashscorecompose.data.fixtures.fixture.mapper

import com.kuba.flashscorecompose.data.fixtures.currentround.local.model.CurrentRoundEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.model.*
import com.kuba.flashscorecompose.data.league.mapper.toLeagueEntity

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun Team.toTeamEntity(): TeamEntity {
    return TeamEntity(
        id = id,
        logo = logo,
        name = name,
        isWinner = winner,
        isHome = false,
        code = "",
        founded = 0,
        isNational = false,
        colors = "",
        leagueId = 0
    )
}

fun Venue.toVenueEntity(): VenueEntity {
    return VenueEntity(city = city.orEmpty(), id = id ?: 0, name = name.orEmpty(), "", 0, "", "")
}

fun Periods.toPeriodsEntity(): PeriodsEntity {
    return PeriodsEntity(first = first, second = second)
}

fun Status.toStatusEntity(): StatusEntity {
    return StatusEntity(
        elapsed = elapsed ?: 0,
        longValue = long.orEmpty(),
        shortValue = short.orEmpty()
    )
}

fun Score.toScoreEntity(): ScoreEntity {
    return ScoreEntity(
        extratime = extratime.toGoalsEntity(),
        fulltime = fulltime.toGoalsEntity(),
        halftime = halftime.toGoalsEntity(),
        penalty = penalty.toGoalsEntity()
    )
}

fun Goals.toGoalsEntity(): GoalsEntity {
    return GoalsEntity(home = home ?: 0, away = away ?: 0)
}

fun FixtureInfo.toFixtureInfoEntity(): FixtureInfoEntity {
    return FixtureInfoEntity(
        date = date.orEmpty(),
        id = id ?: 0,
        referee = referee.orEmpty(),
        status = status.toStatusEntity(),
        timestamp = timestamp ?: 0,
        timezone = timezone.orEmpty(),
        venue = venue.toVenueEntity(),
        periods = periods.toPeriodsEntity()
    )
}

fun FixtureItem.toFixtureEntity(): FixtureEntity {
    return FixtureEntity(
        id = id,
        currentRound = CurrentRoundEntity(0, leagueId, season, round),
        h2h = h2h,
        date = date,
        fixture = fixture.toFixtureInfoEntity(),
        goals = goals.toGoalsEntity(),
        league = league.toLeagueEntity(),
        score = score.toScoreEntity(),
        homeTeam = homeTeam.toTeamEntity(),
        awayTeam = awayTeam.toTeamEntity()
    )
}