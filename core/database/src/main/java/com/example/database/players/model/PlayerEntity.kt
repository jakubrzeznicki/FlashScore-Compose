package com.example.database.players.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.database.teams.model.TeamEntity

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @Embedded(prefix = "team_") val team: TeamEntity,
    @ColumnInfo(name = "season") val season: Int,
    @ColumnInfo(name = "grid") val grid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "number") val number: Int,
    @ColumnInfo(name = "pos") val pos: String,
    @ColumnInfo(name = "firstname") val firstname: String,
    @ColumnInfo(name = "lastname") val lastname: String,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "position") val position: String,
    @Embedded(prefix = "birth_") val birth: BirthEntity,
    @ColumnInfo(name = "nationality") val nationality: String,
    @ColumnInfo(name = "height") val height: String,
    @ColumnInfo(name = "weight") val weight: String,
    @ColumnInfo(name = "injured") val injured: Boolean,
    @ColumnInfo(name = "photo") val photo: String
)