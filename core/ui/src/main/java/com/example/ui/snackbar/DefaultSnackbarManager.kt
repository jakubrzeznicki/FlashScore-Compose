package com.example.ui.snackbar

import androidx.annotation.StringRes
import com.example.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by jrzeznicki on 05/02/2023.
 */
class DefaultSnackbarManager : SnackbarManager {
    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    override val snackbarMessages: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    override fun showMessage(@StringRes message: Int, type: SnackbarMessageType) {
        messages.value = SnackbarMessage.ResourceSnackbar(message, type)
    }

    override fun showMessage(message: SnackbarMessage) {
        messages.value = message
    }

    override fun showSnackbarMessage(message: String?, type: SnackbarMessageType) {
        val snackbarMessage = message?.let { statusMessage ->
            SnackbarMessage.StringSnackbar(statusMessage, type)
        } ?: SnackbarMessage.ResourceSnackbar(R.string.generic_error, type)
        messages.value = snackbarMessage
    }
}
