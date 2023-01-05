package com.kuba.flashscorecompose.data.fixtures.fixture.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.*

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun TeamEntity.toTeam(): Team {
    return Team(id = id, logo = logo, name = name, winner = winner)
}

fun TeamsEntity.toTeams(): Teams {
    return Teams(away = away?.toTeam() ?: Team.EMPTY_TEAM, home = home?.toTeam() ?: Team.EMPTY_TEAM)
}

fun VenueEntity.toVenue(): Venue {
    return Venue(city = city.orEmpty(), id = id ?: 0, name = name.orEmpty())
}

fun StatusEntity.toStatus(): Status {
    return Status(elapsed = elapsed ?: 0, long = long.orEmpty(), short = short.orEmpty())
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

fun LeagueFixtureEntity.toLeagueFixture(): LeagueFixture {
    return LeagueFixture(
        country = country.orEmpty(),
        flag = flag.orEmpty(),
        id = id ?: 0,
        logo = logo.orEmpty(),
        name = name.orEmpty(),
        round = round.orEmpty(),
        season = season ?: 0
    )
}

fun FixtureInfoEntity.toFixtureInfo(): FixtureInfo {
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

fun FixtureEntity.toFixtureItem(): FixtureItem {
    return FixtureItem(
        id = id,
        leagueId = leagueId ?: 0,
        season = season ?: 0,
        round = round.orEmpty(),
        h2h = h2h.orEmpty(),
        fixture = fixture?.toFixtureInfo() ?: FixtureInfo.EMPTY_FIXTURE_INFO,
        goals = goals?.toGoals() ?: Goals.EMPTY_GOALS,
        league = league?.toLeagueFixture() ?: LeagueFixture.EMPTY_LEAGUE_FIXTURE,
        score = score?.toScore() ?: Score.EMPTY_SCORE,
        teams = teams?.toTeams() ?: Teams.EMPTY_TEAMS
    )
}