/**
 * Created by jrzeznicki on 13/03/2023.
 */
object Compose {
    private const val composeVersion = "1.3.3"
    private const val material3Version = "1.1.0-alpha05"
    private const val activityComposeVersion = "1.6.1"
    private const val viewModelComposeVersion = "2.5.1"
    private const val materialIconsVersion = "1.3.1"
    const val material3 = "androidx.compose.material3:material3:$material3Version"
    const val material3WindowSize =
        "androidx.compose.material3:material3-window-size-class:1.0.1"
    const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
    const val ui = "androidx.compose.ui:ui:$composeVersion"
    const val foundation = "androidx.compose.foundation:foundation:$materialIconsVersion"
    const val uiTooling = "androidx.compose.ui:ui-tooling:$composeVersion"
    const val animation = "androidx.compose.animation:animation:$composeVersion"
    const val activityCompose = "androidx.activity:activity-compose:$activityComposeVersion"
    const val viewModelCompose =
        "androidx.lifecycle:lifecycle-viewmodel-compose:$viewModelComposeVersion"
    const val materialIcons =
        "androidx.compose.material:material-icons-extended:$materialIconsVersion"
    const val testJUnit = "androidx.compose.ui:ui-test-junit4:$composeVersion"
    const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
}