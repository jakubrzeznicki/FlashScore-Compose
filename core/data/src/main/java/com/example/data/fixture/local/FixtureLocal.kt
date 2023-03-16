package com.example.data.fixture.local

import com.example.database.RoomStorage
import com.example.database.fixtures.matches.model.FixtureEntity
import com.example.database.leagues.model.LeagueEntity
import com.example.database.teams.model.TeamEntity
import com.example.database.teams.model.VenueEntity
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

    override fun observeFixturesByTeam(teamId: Int, season: Int): Flow<List<FixtureEntity>> {
        return roomStorage.getDatabase().fixtureDao().observeFixturesByTeam(teamId, season)
    }

    override fun observeFixturesByDate(
        date: String,
        countryNames: List<String>
    ): Flow<List<FixtureEntity>> {
        return roomStorage.getDatabase().fixtureDao().observeFixturesByDate(date, countryNames)
    }

    override fun observeFixturesByLeague(
        leagueId: Int
    ): Flow<List<FixtureEntity>> {
        return roomStorage.getDatabase().fixtureDao().observeFixturesByLeague(leagueId)
    }

    override fun observeFixturesLive(): Flow<List<FixtureEntity>> {
        return roomStorage.getDatabase().fixtureDao().observeFixturesLive()
    }

    override fun observeFavoriteFixtures(ids: List<Int>): Flow<List<FixtureEntity>> {
        return roomStorage.getDatabase().fixtureDao().observeFavoriteFixtures(ids)
    }

    override fun observeFixtureById(id: Int): Flow<FixtureEntity?> {
        return roomStorage.getDatabase().fixtureDao().observeFixtureById(id)
    }

    override suspend fun saveFixtures(fixtures: List<FixtureEntity>) {
        roomStorage.getDatabase().fixtureDao().saveFixtures(fixtures = fixtures)
    }

    override suspend fun saveVenues(venues: List<VenueEntity>) {
        roomStorage.getDatabase().venueDao().saveVenue(venues)
    }

    override suspend fun saveLeagues(leagues: List<LeagueEntity>) {
        roomStorage.getDatabase().leagueDao().saveLeagues(leagues)
    }

    override suspend fun saveTeams(teams: List<TeamEntity>) {
        roomStorage.getDatabase().teamDao().saveTeams(teams)
    }
}
