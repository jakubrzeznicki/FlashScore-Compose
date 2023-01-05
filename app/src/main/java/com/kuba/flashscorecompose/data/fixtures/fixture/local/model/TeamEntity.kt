package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team")
data class TeamEntity(
    @PrimaryKey @ColumnInfo(name = "team_id") val id: Int,
    @ColumnInfo(name = "logo") val logo: String?,
    @ColumnInfo(name = "team_name") val name: String?,
    @ColumnInfo(name = "winner") val winner: Boolean?,
    @ColumnInfo(name = "is_home") val isHome: Boolean?,
    @ColumnInfo(name = "code") val code: String?,
    @ColumnInfo(name = "founded") val founded: Int?,
    @ColumnInfo(name = "is_national") val isNational: Boolean?,
    @ColumnInfo(name = "colors") val String: String?,
    @ColumnInfo(name = "league_id") val leagueId: Int?,
)