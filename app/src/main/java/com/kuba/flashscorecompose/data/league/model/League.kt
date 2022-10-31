package com.kuba.flashscorecompose.data.league.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by jrzeznicki on 10/1/2022
 */
@Entity(tableName = "leagues")
data class League(
    @ColumnInfo(name = "countryId") @SerializedName("country_id") val countryId: String,
    @ColumnInfo(name = "countryName") @SerializedName("country_name") val countryName: String,
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("league_id") val id: String,
    @ColumnInfo(name = "name") @SerializedName("league_name") val name: String,
    @ColumnInfo(name = "season") @SerializedName("league_season") val season: String,
    @ColumnInfo(name = "countryLogo") @SerializedName("country_logo") var countryLogo: String? = null,
    @ColumnInfo(name = "logo") @SerializedName("league_logo") var logo: String? = null,
)