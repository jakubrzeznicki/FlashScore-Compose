package com.kuba.flashscorecompose.data.fixtures.currentround.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jrzeznicki on 03/01/2023.
 */
data class CurrentRoundDataDto(
    @SerializedName("parameters") val parameters: ParametersDto?,
    @SerializedName("response") val response: List<CurrentRoundDto>?
)