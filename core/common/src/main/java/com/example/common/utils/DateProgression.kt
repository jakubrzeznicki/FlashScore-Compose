package com.example.common.utils

import java.time.LocalDate

/**
 * Created by jrzeznicki on 19/12/2022.
 */
class DateProgression(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    private val stepDays: Long = 1
) : Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> = DateIterator(start, endInclusive, stepDays)
}
