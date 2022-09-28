package com.kuba.flashscorecompose.data.country.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jrzeznicki on 9/7/2022
 */
@Entity(tableName = "countries")
data class Country(

    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("country_id") val id: String,
    @ColumnInfo(name = "logo") @SerializedName("country_logo") var logo: String? = null,
    @ColumnInfo(name = "name") @SerializedName("country_name") var name: String? = null
)