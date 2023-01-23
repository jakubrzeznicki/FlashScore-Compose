package com.kuba.flashscorecompose.utils

import java.util.*

/**
 * Created by jrzeznicki on 21/01/2023.
 */
fun String?.containsQuery(query: String) =
    this.orEmpty()
        .lowercase(Locale.getDefault())
        .contains(query.lowercase(Locale.getDefault()))