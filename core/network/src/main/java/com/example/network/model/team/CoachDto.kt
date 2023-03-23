package com.example.network.model.team

import com.example.network.model.player.BirthDto
import com.google.gson.annotations.SerializedName

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
