package com.kuba.flashscorecompose.matchdetails.tabs

import androidx.compose.runtime.Composable
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.matchdetails.screen.EventData
import com.kuba.flashscorecompose.matchdetails.screen.HeadToHeadScreen
import com.kuba.flashscorecompose.matchdetails.screen.LineUpScreen
import com.kuba.flashscorecompose.matchdetails.screen.MatchDetailsScreen

/**
 * Created by jrzeznicki on 23/12/2022.
 */
typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int, var title: String, var screen: ComposableFun) {
    class MatchDetail(eventData: EventData) :
        TabItem(
            R.drawable.ic_profile,
            "Match Detail",
            { MatchDetailsScreen(eventData.homeTeamStatistics, eventData.awayTeamStatistics) })

    class LineUp(eventData: EventData) :
        TabItem(R.drawable.ic_standings, "Line Up", { LineUpScreen(eventData.lineUp) })

    object H2H : TabItem(R.drawable.ic_explore, "H2H", { HeadToHeadScreen() })
}