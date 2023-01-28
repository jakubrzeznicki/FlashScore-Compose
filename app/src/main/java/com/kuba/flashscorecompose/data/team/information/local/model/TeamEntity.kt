package com.kuba.flashscorecompose.data.team.information.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.ColorsEntity

@Entity(tableName = "team")
data class TeamEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "logo") val logo: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "winner") val isWinner: Boolean,
    @ColumnInfo(name = "is_home") val isHome: Boolean,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "founded") val founded: Int,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "is_national") val isNational: Boolean,
    @Embedded(prefix = "colors_") val colors: ColorsEntity,
    @ColumnInfo(name = "league_id") val leagueId: Int
)