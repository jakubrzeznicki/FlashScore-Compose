package com.kuba.flashscorecompose.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ui.theme.FlashScoreComposeTheme
import com.kuba.flashscorecompose.BuildConfig
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashScoreComposeTheme {
                FlashScoreApp()
            }
        }
        AppCenter.start(
            application,
            BuildConfig.APP_CENTER_KEY,
            Analytics::class.java,
            Crashes::class.java
        )
    }
}
