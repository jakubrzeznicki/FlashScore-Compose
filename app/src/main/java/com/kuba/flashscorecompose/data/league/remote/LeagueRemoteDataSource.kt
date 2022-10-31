package com.kuba.flashscorecompose.data.league.remote

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.utils.RepositoryResult

/**
 * Created by jrzeznicki on 10/3/2022
 */
interface LeagueRemoteDataSource {
    suspend fun loadLeagues(countryId: String): RepositoryResult<List<League>>
}