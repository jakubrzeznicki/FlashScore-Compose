package com.kuba.flashscorecompose.data.league.model

/**
 * Created by jrzeznicki on 10/1/2022
 */
data class League(
    val id: Int,
    val name: String,
    val type: String,
    val logo: String,
    val countryCode: String,
    val countryName: String,
    val countryFlag: String
) {
    companion object {
        val EMPTY_LEAGUE = League(0, "", "", "", "", "", "")
    }
}