package com.kuba.flashscorecompose.ui.component.snackbar

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by jrzeznicki on 19/02/2023.
 */
interface SnackbarManager {
    val snackbarMessages: StateFlow<SnackbarMessage?>
    fun showMessage(@StringRes message: Int, type: SnackbarMessageType)
    fun showMessage(message: SnackbarMessage)
    fun showSnackbarMessage(message: String?, type: SnackbarMessageType)
}