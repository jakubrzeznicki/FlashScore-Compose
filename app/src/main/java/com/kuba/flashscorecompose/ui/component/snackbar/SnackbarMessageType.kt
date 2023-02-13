package com.kuba.flashscorecompose.ui.component.snackbar

/**
 * Created by jrzeznicki on 10/02/2023.
 */
sealed interface SnackbarMessageType {
    object Error : SnackbarMessageType
    object Success : SnackbarMessageType
}