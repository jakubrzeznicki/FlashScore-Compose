package com.kuba.flashscorecompose.data.fixtures.fixture.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.*
import com.kuba.flashscorecompose.data.fixtures.fixture.model.*

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun Team.toTeamEntity(): TeamEntity {
    return TeamEntity(id = id, logo = logo, name = name, winner = winner, isHome = false)
}

fun Teams.toTeamsEntity(): TeamsEntity {
    return TeamsEntity(away = away.toTeamEntity(), home = home.toTeamEntity())
}

fun Venue.toVenueEntity(): VenueEntity {
    return VenueEntity(city = city.orEmpty(), id = id ?: 0, name = name.orEmpty())
}

fun Status.toStatusEntity(): StatusEntity {
    return StatusEntity(elapsed = elapsed ?: 0, long = long.orEmpty(), short = short.orEmpty())
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

fun LeagueFixture.toLeagueFixtureEntity(): LeagueFixtureEntity {
    return LeagueFixtureEntity(
        country = country.orEmpty(),
        flag = flag.orEmpty(),
        id = id ?: 0,
        logo = logo.orEmpty(),
        name = name.orEmpty(),
        round = round.orEmpty(),
        season = season ?: 0
    )
}

fun FixtureInfo.toFixtureInfoEntity(): FixtureInfoEntity {
    return FixtureInfoEntity(
        date = date.orEmpty(),
        id = id ?: 0,
        referee = referee.orEmpty(),
        status = status.toStatusEntity(),
        timestamp = timestamp ?: 0,
        timezone = timezone.orEmpty(),
        venue = venue.toVenueEntity()
    )
}

fun FixtureItem.toFixtureEntity(): FixtureEntity {
    return FixtureEntity(
        id = id,
        leagueId = leagueId,
        season = season,
        round = round,
        h2h = h2h,
        fixture = fixture.toFixtureInfoEntity(),
        goals = goals.toGoalsEntity(),
        league = league.toLeagueFixtureEntity(),
        score = score.toScoreEntity(),
        teams = teams.toTeamsEntity()
    )
}