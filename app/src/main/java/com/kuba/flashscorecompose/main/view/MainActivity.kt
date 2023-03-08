package com.kuba.flashscorecompose.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
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
            "bd4b6475-87b1-4e34-b8d9-635973489f20",
            Analytics::class.java,
            Crashes::class.java
        )
    }
}
