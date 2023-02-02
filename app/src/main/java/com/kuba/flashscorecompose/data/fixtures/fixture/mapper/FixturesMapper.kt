package com.kuba.flashscorecompose.data.fixtures.fixture.mapper

import com.kuba.flashscorecompose.data.fixtures.currentround.local.model.CurrentRoundEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.model.*
import com.kuba.flashscorecompose.data.league.mapper.toLeagueEntity
import com.kuba.flashscorecompose.data.team.information.mapper.toTeamEntity
import com.kuba.flashscorecompose.data.team.information.mapper.toVenueEntity

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun Periods.toPeriodsEntity(): PeriodsEntity {
    return PeriodsEntity(first = first, second = second)
}

fun Status.toStatusEntity(): StatusEntity {
    return StatusEntity(
        elapsed = elapsed,
        longValue = long,
        shortValue = short
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
    return GoalsEntity(home = home, away = away)
}

fun PlayerColor.toPlayerColorEntity(): PlayerColorEntity {
    return PlayerColorEntity(border = border, number, primary)
}

fun Colors.toColorsEntity(): ColorsEntity {
    return ColorsEntity(
        goalkeeper = goalkeeper.toPlayerColorEntity(),
        player = player.toPlayerColorEntity()
    )
}

fun FixtureInfo.toFixtureInfoEntity(teamId: Int?): FixtureInfoEntity {
    return FixtureInfoEntity(
        date = date,
        id = id,
        referee = referee,
        status = status.toStatusEntity(),
        timestamp = timestamp,
        timezone = timezone,
        venue = venue.toVenueEntity(teamId),
        periods = periods.toPeriodsEntity(),
        isLive = isLive,
        isStarted = isStarted
    )
}

fun FixtureItem.toFixtureEntity(): FixtureEntity {
    return FixtureEntity(
        id = id,
        currentRound = CurrentRoundEntity(
            0,
            league.id,
            league.season,
            league.round
        ),
        h2h = h2h,
        date = date,
        fixture = fixture.toFixtureInfoEntity(homeTeam.id),
        goals = goals.toGoalsEntity(),
        league = league.toLeagueEntity(),
        score = score.toScoreEntity(),
        homeTeam = homeTeam.toTeamEntity(league.id, league.season),
        awayTeam = awayTeam.toTeamEntity(league.id, league.season)
    )
}