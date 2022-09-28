package com.kuba.flashscorecompose.data.country.remote

import com.kuba.flashscorecompose.api.FlashScoreApi
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 9/7/2022
 */
class CountryRemote(private val flashScoreApi: FlashScoreApi) : CountryRemoteDataSource {

    override suspend fun loadCountries(): RepositoryResult<List<Country>> {
        val result = flashScoreApi.getCountries()
        return try {
            RepositoryResult.Success(result.body().orEmpty())
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}