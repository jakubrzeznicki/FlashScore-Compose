package com.kuba.flashscorecompose.utils

import java.time.LocalDate

/**
 * Created by jrzeznicki on 19/12/2022.
 */
operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)