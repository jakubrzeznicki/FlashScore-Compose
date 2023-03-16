package com.example.ui.composables.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.R

/**
 * Created by jrzeznicki on 23/12/2022.
 */

sealed class TabItem(var icon: ImageVector, var titleId: Int, var screen: @Composable () -> Unit) {

    sealed interface FixtureDetails {
        class Statistics(screen: @Composable () -> Unit) : TabItem(
            Icons.Default.QueryStats,
            R.string.statistics,
            { screen() }
        )

        class LineUp(screen: @Composable () -> Unit) :
            TabItem(
                Icons.Default.Square,
                R.string.lineups,
                { screen() }
            )

        class HeadToHead(screen: @Composable () -> Unit) : TabItem(
            Icons.Default.Headset,
            R.string.head_to_head,
            { screen() }
        )
    }

    sealed interface TeamDetails {

        class Information(screen: @Composable () -> Unit) : TabItem(
            Icons.Default.Info,
            R.string.informations,
            { screen() }
        )

        class Players(screen: @Composable () -> Unit) : TabItem(
            Icons.Default.Info,
            R.string.players,
            { screen() }
        )

        class Fixtures(screen: @Composable () -> Unit) : TabItem(
            Icons.Default.FiberDvr,
            R.string.fixtures,
            { screen() }
        )

        class Injuries(screen: @Composable () -> Unit) : TabItem(
            Icons.Default.PersonalInjury,
            R.string.injuries,
            {}
        )

        class Transfers(screen: @Composable () -> Unit) : TabItem(
            Icons.Default.Transform,
            R.string.transfers,
            {}
        )
    }

    sealed interface Profile {
        class Details(screen: @Composable () -> Unit) :
            TabItem(Icons.Default.Info, R.string.my_profile, { screen() })

        class Activity(screen: @Composable () -> Unit) :
            TabItem(Icons.Default.LocalActivity, R.string.activity, { })

        class Settings(screen: @Composable () -> Unit) :
            TabItem(
                Icons.Default.Settings,
                R.string.settings,
                { screen() }
            )
    }
}
