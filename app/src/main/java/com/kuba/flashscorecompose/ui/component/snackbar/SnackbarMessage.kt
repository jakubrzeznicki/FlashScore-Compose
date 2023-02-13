package com.kuba.flashscorecompose.ui.component.snackbar

import android.content.res.Resources
import androidx.annotation.StringRes

/**
 * Created by jrzeznicki on 05/02/2023.
 */
sealed class SnackbarMessage(open val type: SnackbarMessageType) {
    class StringSnackbar(
        val message: String,
        override val type: SnackbarMessageType
    ) : SnackbarMessage(type)

    class ResourceSnackbar(@StringRes val message: Int, override val type: SnackbarMessageType) :
        SnackbarMessage(type)

    companion object {
        fun SnackbarMessage.toMessage(resources: Resources): String {
            return when (this) {
                is StringSnackbar -> this.message
                is ResourceSnackbar -> resources.getString(this.message)
            }
        }
    }
}
