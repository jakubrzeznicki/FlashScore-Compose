package com.kuba.flashscorecompose.data.team.information.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuba.flashscorecompose.data.players.local.model.BirthEntity

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Entity(tableName = "coach")
data class CoachEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "team_id") val teamId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "photo") val photo: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "nationality") val nationality: String,
    @ColumnInfo(name = "height") val height: String,
    @ColumnInfo(name = "weight") val weight: String,
    @Embedded(prefix = "birth_") val birth: BirthEntity
)