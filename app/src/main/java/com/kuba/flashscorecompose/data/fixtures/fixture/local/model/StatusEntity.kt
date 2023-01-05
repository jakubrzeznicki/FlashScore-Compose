package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.ColumnInfo

data class StatusEntity(
    @ColumnInfo(name = "elapsed") val elapsed: Int,
    @ColumnInfo(name = "long_value") val long: String,
    @ColumnInfo(name = "short_value") val short: String
)
