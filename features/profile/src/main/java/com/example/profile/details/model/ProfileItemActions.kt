package com.example.profile.details.model

/**
 * Created by jrzeznicki on 08/02/2023.
 */
data class ProfileItemActions(
    val onItemClick: () -> Unit,
    val onValueChange: (String) -> Unit,
    val onDoneClick: () -> Unit
)
