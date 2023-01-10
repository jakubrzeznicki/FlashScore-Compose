package com.kuba.flashscorecompose.data.fixtures.fixture.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.FixtureEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.TeamEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.VenueEntity
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class FixtureLocal(private val roomStorage: RoomStorage) : FixtureLocalDataSource {
    override fun observeFixturesFilteredByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Flow<List<FixtureEntity>> {
        return roomStorage.getDatabase().fixtureDao()
            .observeFixturesFilteredByRound(leagueId, season, round)
    }

    override fun observeFixturesHeadToHead(h2h: String): Flow<List<FixtureEntity>> {
        return roomStorage.getDatabase().fixtureDao().observeFixturesHeadToHead(h2h)
    }

    override fun observeFixturesByDate(date: String): Flow<List<FixtureEntity>> {
        return roomStorage.getDatabase().fixtureDao().observeFixturesByDate(date)
    }

    override fun observeXLastFixtures(count: Int, countryNames: List<String>): Flow<List<FixtureEntity>> {
        return roomStorage.getDatabase().fixtureDao().observeXLastFixtures(count, countryNames)
    }

    override fun saveFixtures(fixtures: List<FixtureEntity>) {
        roomStorage.getDatabase().fixtureDao().saveFixtures(fixtures = fixtures)
    }

    override fun saveVenues(venues: List<VenueEntity>) {
        roomStorage.getDatabase().venueDao().saveVenue(venues)
    }

    override fun saveLeagues(leagues: List<LeagueEntity>) {
        roomStorage.getDatabase().leagueDao().saveLeagues(leagues)
    }

    override fun saveTeams(teams: List<TeamEntity>) {
        roomStorage.getDatabase().teamDao().saveTeams(teams)
    }

    override fun deleteFixturesByRound(leagueId: Int, season: Int, round: String) {
        roomStorage.getDatabase().fixtureDao().deleteByRound(leagueId, season, round)
    }

    override fun deleteFixturesByH2H(h2h: String) {
        roomStorage.getDatabase().fixtureDao().deleteByH2H(h2h)
    }
}