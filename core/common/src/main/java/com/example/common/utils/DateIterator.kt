package com.example.common.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

/**
 * Created by jrzeznicki on 19/12/2022.
 */
class DateIterator(
    startDate: LocalDate,
    private val endDateInclusive: LocalDate,
    private val stepDays: Long
) : Iterator<LocalDate> {
    private var currentDate = startDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun hasNext() = currentDate <= endDateInclusive

    @RequiresApi(Build.VERSION_CODES.O)
    override fun next(): LocalDate {
        val next = currentDate
        currentDate = currentDate.plusDays(stepDays)
        return next
    }
}
