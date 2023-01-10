package com.kuba.flashscorecompose.data.fixtures.fixture.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.model.*
import com.kuba.flashscorecompose.data.league.mapper.toLeague
import com.kuba.flashscorecompose.data.league.model.League

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun TeamEntity.toTeam(): Team {
    return Team(id = id, logo = logo.orEmpty(), name = name.orEmpty(), winner = isWinner ?: false)
}

fun VenueEntity.toVenue(): Venue {
    return Venue(city = city.orEmpty(), id = id ?: 0, name = name.orEmpty())
}

fun PeriodsEntity.toPeriods(): Periods {
    return Periods(first ?: 0, second ?: 0)
}

fun StatusEntity.toStatus(): Status {
    return Status(elapsed = elapsed ?: 0, long = longValue.orEmpty(), short = shortValue.orEmpty())
}

fun ScoreEntity.toScore(): Score {
    return Score(
        extratime = extratime?.toGoals() ?: Goals.EMPTY_GOALS,
        fulltime = fulltime?.toGoals() ?: Goals.EMPTY_GOALS,
        halftime = halftime?.toGoals() ?: Goals.EMPTY_GOALS,
        penalty = penalty?.toGoals() ?: Goals.EMPTY_GOALS
    )
}

fun GoalsEntity.toGoals(): Goals {
    return Goals(home = home ?: 0, away = away ?: 0)
}

fun FixtureInfoEntity.toFixtureInfo(): FixtureInfo {
    return FixtureInfo(
        date = date.orEmpty(),
        id = id ?: 0,
        referee = referee.orEmpty(),
        status = status?.toStatus() ?: Status.EMPTY_STATUS,
        timestamp = timestamp ?: 0,
        timezone = timezone.orEmpty(),
        venue = venue?.toVenue() ?: Venue.EMPTY_VENUE,
        periods = periods?.toPeriods() ?: Periods.EMPTY_PERIODS
    )
}

fun FixtureEntity.toFixtureItem(): FixtureItem {
    return FixtureItem(
        id = id,
        leagueId = currentRound.leagueId,
        season = currentRound.season,
        round = currentRound.round,
        h2h = h2h.orEmpty(),
        date = date.orEmpty(),
        fixture = fixture?.toFixtureInfo() ?: FixtureInfo.EMPTY_FIXTURE_INFO,
        goals = goals?.toGoals() ?: Goals.EMPTY_GOALS,
        league = league?.toLeague() ?: League.EMPTY_LEAGUE,
        score = score?.toScore() ?: Score.EMPTY_SCORE,
        homeTeam = homeTeam.toTeam(),
        awayTeam = awayTeam.toTeam()
    )
}