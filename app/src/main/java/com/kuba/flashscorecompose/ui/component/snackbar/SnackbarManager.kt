package com.kuba.flashscorecompose.ui.component.snackbar

import androidx.annotation.StringRes
import com.kuba.flashscorecompose.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by jrzeznicki on 05/02/2023.
 */
object SnackbarManager {
    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    val snackbarMessages: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    fun showMessage(@StringRes message: Int, type: SnackbarMessageType) {
        messages.value = SnackbarMessage.ResourceSnackbar(message, type)
    }

    fun showMessage(message: SnackbarMessage) {
        messages.value = message
    }

    fun String?.showSnackbarMessage(type: SnackbarMessageType) {
        val snackbarMessage = this?.let { statusMessage ->
            SnackbarMessage.StringSnackbar(statusMessage, type)
        } ?: SnackbarMessage.ResourceSnackbar(R.string.generic_error, type)
        showMessage(snackbarMessage)
    }
}
