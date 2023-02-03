package com.kuba.flashscorecompose.data.fixtures.fixture.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.model.*
import com.kuba.flashscorecompose.data.league.mapper.toLeague
import com.kuba.flashscorecompose.data.team.information.mapper.toTeam
import com.kuba.flashscorecompose.data.team.information.mapper.toVenue

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun ColorsEntity.toColors(): Colors {
    return Colors(goalkeeper.toPlayerColor(), player = player.toPlayerColor())
}

fun PlayerColorEntity.toPlayerColor(): PlayerColor {
    return PlayerColor(border = border, number = number, primary = primary)
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
        "",
        isLive = isLive,
        isStarted = isStarted
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