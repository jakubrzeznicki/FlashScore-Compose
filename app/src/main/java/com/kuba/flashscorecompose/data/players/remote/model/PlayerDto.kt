package com.kuba.flashscorecompose.data.players.remote.model

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("grid") val grid: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("firstname") val firstname: String?,
    @SerializedName("lastname") val lastname: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("number") val number: Int?,
    @SerializedName("pos") val pos: String?,
    @SerializedName("position") val position: String?,
    @SerializedName("birth") val birth: BirthDto?,
    @SerializedName("nationality") val nationality: String?,
    @SerializedName("height") val height: String?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("injured") val injured: Boolean?,
    @SerializedName("photo") val photo: String?
)
