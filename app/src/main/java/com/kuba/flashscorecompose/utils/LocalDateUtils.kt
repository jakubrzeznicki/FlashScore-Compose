package com.kuba.flashscorecompose.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by jrzeznicki on 19/12/2022.
 */
operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

fun LocalDateTime.format(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return format(formatter)
}
