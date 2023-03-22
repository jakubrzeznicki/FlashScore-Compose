package com.example.network.model.team

import com.google.gson.annotations.SerializedName

data class TeamDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("logo") val logo: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("winner") val winner: Boolean?,
    @SerializedName("code") val code: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("founded") val founded: Int?,
    @SerializedName("national") val national: Boolean?
)
