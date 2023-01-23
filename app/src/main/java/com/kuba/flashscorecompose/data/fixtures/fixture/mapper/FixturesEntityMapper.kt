package com.kuba.flashscorecompose.data.fixtures.fixture.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.model.*
import com.kuba.flashscorecompose.data.league.mapper.toLeague

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun TeamEntity.toTeam(): Team {
    return Team(
        id = id,
        logo = logo,
        name = name,
        isWinner = isWinner,
        code = code,
        founded = founded,
        isNational = isNational,
        colors = colors.toColors()
    )
}

fun ColorsEntity.toColors(): Colors {
    return Colors(goalkeeper.toPlayerColor(), player = player.toPlayerColor())
}

fun PlayerColorEntity.toPlayerColor(): PlayerColor {
    return PlayerColor(border = border, number = number, primary = primary)
}

fun VenueEntity.toVenue(): Venue {
    return Venue(
        city = city,
        id = id,
        name = name,
        address = address,
        capacity = capacity,
        surface = surface,
        image = image
    )
}

fun PeriodsEntity.toPeriods(): Periods {
    return Periods(first, second)
}

fun StatusEntity.toStatus(): Status {
    return Status(elapsed = elapsed, long = longValue, short = shortValue)
}

fun ScoreEntity.toScore(): Score {
    return Score(
        extratime = extratime.toGoals(),
        fulltime = fulltime.toGoals(),
        halftime = halftime.toGoals(),
        penalty = penalty.toGoals()
    )
}

fun GoalsEntity.toGoals(): Goals {
    return Goals(home = home, away = away)
}

fun FixtureInfoEntity.toFixtureInfo(): FixtureInfo {
    return FixtureInfo(
        date = date,
        id = id,
        referee = referee,
        status = status.toStatus(),
        timestamp = timestamp,
        timezone = timezone,
        venue = venue.toVenue(),
        periods = periods.toPeriods(),
        ""
    )
}

fun FixtureEntity.toFixtureItem(): FixtureItem {
    return FixtureItem(
        id = id,
        season = currentRound.season,
        round = currentRound.round,
        h2h = h2h,
        date = date,
        fixture = fixture.toFixtureInfo(),
        goals = goals.toGoals(),
        league = league.toLeague(),
        score = score.toScore(),
        homeTeam = homeTeam.toTeam(),
        awayTeam = awayTeam.toTeam()
    )
}