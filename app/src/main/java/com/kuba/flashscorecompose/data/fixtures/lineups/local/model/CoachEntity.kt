package com.kuba.flashscorecompose.data.fixtures.lineups.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Entity(tableName = "coach")
data class CoachEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "photo") val photo: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "nationality") val nationality: String
)