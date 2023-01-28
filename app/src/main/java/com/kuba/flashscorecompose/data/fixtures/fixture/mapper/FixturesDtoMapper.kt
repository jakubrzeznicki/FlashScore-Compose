package com.kuba.flashscorecompose.data.fixtures.fixture.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.*
import com.kuba.flashscorecompose.data.league.mapper.toLeague
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.team.information.mapper.toTeam
import com.kuba.flashscorecompose.data.team.information.mapper.toVenue
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.model.Venue

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun PeriodsDto.toPeriods(): Periods {
    return Periods(first = first ?: 0, second = second ?: 0)
}

fun StatusDto.toStatus(): Status {
    return Status(elapsed = elapsed ?: 0, long = long.orEmpty(), short = short.orEmpty())
}

fun ScoreDto.toScore(): Score {
    return Score(
        extratime = extratime?.toGoals() ?: Goals.EMPTY_GOALS,
        fulltime = fulltime?.toGoals() ?: Goals.EMPTY_GOALS,
        halftime = halftime?.toGoals() ?: Goals.EMPTY_GOALS,
        penalty = penalty?.toGoals() ?: Goals.EMPTY_GOALS
    )
}

fun GoalsDto.toGoals(): Goals {
    return Goals(home = home ?: 0, away = away ?: 0)
}

fun PlayerColorDto.toPlayerColor(): PlayerColor {
    return PlayerColor(border = border.orEmpty(), number.orEmpty(), primary.orEmpty())
}

fun ColorsDto.toColors(): Colors {
    return Colors(
        goalkeeper = goalkeeper?.toPlayerColor() ?: PlayerColor.EMPTY_PLAYER_COLOR,
        player = player?.toPlayerColor() ?: PlayerColor.EMPTY_PLAYER_COLOR
    )
}

fun FixtureInfoDto.toFixtureInfo(teamId: Int?): FixtureInfo {
    return FixtureInfo(
        date = date.orEmpty(),
        id = id ?: 0,
        referee = referee.orEmpty(),
        status = status?.toStatus() ?: Status.EMPTY_STATUS,
        timestamp = timestamp ?: 0,
        timezone = timezone.orEmpty(),
        venue = venue?.toVenue(teamId) ?: Venue.EMPTY_VENUE,
        periods = periods?.toPeriods() ?: Periods.EMPTY_PERIODS,
        year = ""
    )
}

fun FixtureDto.toFixtureItem(
    season: Int? = null,
    round: String? = null,
    h2h: String? = null,
    date: String? = null
): FixtureItem {
    return FixtureItem(
        id = fixture?.id ?: 0,
        season = season ?: league?.season ?: 0,
        round = round ?: league?.round.orEmpty(),
        h2h = h2h ?: "${teams?.home?.id ?: 0}-${teams?.away?.id ?: 0}",
        date = date ?: fixture?.date.orEmpty(),
        fixture = fixture?.toFixtureInfo(teams?.home?.id) ?: FixtureInfo.EMPTY_FIXTURE_INFO,
        goals = goals?.toGoals() ?: Goals.EMPTY_GOALS,
        league = league?.toLeague() ?: League.EMPTY_LEAGUE,
        score = score?.toScore() ?: Score.EMPTY_SCORE,
        homeTeam = teams?.home?.toTeam() ?: Team.EMPTY_TEAM,
        awayTeam = teams?.away?.toTeam() ?: Team.EMPTY_TEAM
    )
}