package com.kuba.flashscorecompose.data.team.information.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.BirthDto

data class CoachDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("firstname") val firstname: String?,
    @SerializedName("lastname") val lastname: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("nationality") val nationality: String?,
    @SerializedName("height") val height: String?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("birth") val birth: BirthDto?
)