package com.kuba.flashscorecompose.data.fixtures.currentround.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jrzeznicki on 03/01/2023.
 */
data class ParametersDto(
    @SerializedName("current") val current: Boolean?,
    @SerializedName("league") val league: Int?,
    @SerializedName("season") val season: Int?
)
