package com.kuba.flashscorecompose.data.league.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by jrzeznicki on 10/1/2022
 */

@Parcelize
data class League(
    val id: Int,
    val name: String,
    val type: String,
    val logo: String,
    val countryCode: String,
    val countryName: String,
    val countryFlag: String,
    val season: Int,
    val round: String
) : Parcelable {
    companion object {
        val EMPTY_LEAGUE = League(0, "", "", "", "", "", "", 0, "")
    }
}