package com.kuba.flashscorecompose.data.fixtures.fixture.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.*
import com.kuba.flashscorecompose.data.league.mapper.toLeague
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.league.remote.model.LeagueDto

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun TeamDto.toTeam(): Team {
    return Team(
        id = id ?: 0,
        logo = logo.orEmpty(),
        name = name.orEmpty(),
        winner = winner ?: false
    )
}

fun VenueDto.toVenue(): Venue {
    return Venue(city = city.orEmpty(), id = id ?: 0, name = name.orEmpty())
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

fun LeagueDto.toLeague(): League {
    return League(
        id = id ?: 0,
        logo = logo.orEmpty(),
        name = name.orEmpty(),
        type = type.orEmpty(),
        countryCode = ""
    )
}

fun FixtureInfoDto.toFixtureInfo(): FixtureInfo {
    return FixtureInfo(
        date = date.orEmpty(),
        id = id ?: 0,
        referee = referee.orEmpty(),
        status = status?.toStatus() ?: Status.EMPTY_STATUS,
        timestamp = timestamp ?: 0,
        timezone = timezone.orEmpty(),
        venue = venue?.toVenue() ?: Venue.EMPTY_VENUE
    )
}

fun FixtureDto.toFixtureItem(leagueId: Int, season: Int, round: String): FixtureItem {
    return FixtureItem(
        id = 0, // lub tworzenie randoma
        leagueId = leagueId,
        season = season,
        round = round,
        h2h = "",
        fixture = fixture?.toFixtureInfo() ?: FixtureInfo.EMPTY_FIXTURE_INFO,
        goals = goals?.toGoals() ?: Goals.EMPTY_GOALS,
        league = league?.toLeague() ?: League.EMPTY_LEAGUE,
        score = score?.toScore() ?: Score.EMPTY_SCORE,
        homeTeam = teams?.home?.toTeam() ?: Team.EMPTY_TEAM,
        awayTeam = teams?.away?.toTeam() ?: Team.EMPTY_TEAM
    )
}

fun FixtureDto.toFixtureItem(h2h: String): FixtureItem {
    return FixtureItem(
        id = 0, // lub tworzenie randoma
        leagueId = 0,
        season = 0,
        round = "",
        h2h = h2h,
        fixture = fixture?.toFixtureInfo() ?: FixtureInfo.EMPTY_FIXTURE_INFO,
        goals = goals?.toGoals() ?: Goals.EMPTY_GOALS,
        league = league?.toLeague() ?: League.EMPTY_LEAGUE,
        score = score?.toScore() ?: Score.EMPTY_SCORE,
        homeTeam = teams?.home?.toTeam() ?: Team.EMPTY_TEAM,
        awayTeam = teams?.away?.toTeam() ?: Team.EMPTY_TEAM
    )
}