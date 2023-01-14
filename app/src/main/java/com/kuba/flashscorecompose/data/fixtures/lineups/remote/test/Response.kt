package com.kuba.flashscorecompose.data.fixtures.lineups.remote.test

import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.StartXIDto
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.SubstituteDto

data class Response(
    val coach: Coach,
    val formation: String,
    val startXI: List<StartXIDto>,
    val substitutes: List<SubstituteDto>,
    val team: Team
)