package com.kuba.flashscorecompose.data.fixtures.statistics.local.model

import androidx.room.ColumnInfo

/**
 * Created by jrzeznicki on 04/01/2023.
 */
data class StatisticRowEntity(
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "value") val value: Int,
)