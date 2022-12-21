package com.kuba.flashscorecompose.leagues.model

import java.time.LocalDate

/**
 * Created by jrzeznicki on 19/12/2022.
 */
data class DayItem(
    val index: Int,
    val weekDay: String,
    val formattedDate: String,
    val date: LocalDate,
    val isSelected: Boolean = false
)