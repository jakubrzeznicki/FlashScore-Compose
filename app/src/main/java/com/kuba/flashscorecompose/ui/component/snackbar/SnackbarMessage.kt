package com.kuba.flashscorecompose.ui.component.snackbar

import android.content.res.Resources
import android.util.Log
import androidx.annotation.StringRes
import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 05/02/2023.
 */
sealed class SnackbarMessage {
    class StringSnackbar(val message: String) : SnackbarMessage()
    class ResourceSnackbar(@StringRes val message: Int) : SnackbarMessage()

    companion object {
        fun SnackbarMessage.toMessage(resources: Resources): String {
            return when (this) {
                is StringSnackbar -> this.message
                is ResourceSnackbar -> {
                    Log.d("TEST_LOG", "this.message-= ${this.message}")
                    resources.getString(this.message)
                }
            }
        }

        fun Throwable.toSnackbarMessage(): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackbar(message)
            else ResourceSnackbar(R.string.generic_error)
        }
    }
}
