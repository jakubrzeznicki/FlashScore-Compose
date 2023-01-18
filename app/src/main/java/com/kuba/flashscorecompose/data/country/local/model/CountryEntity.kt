package com.kuba.flashscorecompose.data.country.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jrzeznicki on 9/7/2022
 */
@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "flag") val flag: String
)