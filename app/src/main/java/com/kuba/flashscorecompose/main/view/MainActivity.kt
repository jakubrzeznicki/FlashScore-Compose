package com.kuba.flashscorecompose.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kuba.flashscorecompose.NavGraphs
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashScoreComposeTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
        AppCenter.start(
            application, "7743e143-e356-4f08-9cb0-c04575a40543",
            Analytics::class.java, Crashes::class.java
        )
    }
}