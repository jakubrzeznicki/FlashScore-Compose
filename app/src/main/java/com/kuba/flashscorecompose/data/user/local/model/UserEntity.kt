package com.kuba.flashscorecompose.data.user.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jrzeznicki on 08/02/2023.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val address: String,
    val photoUri: String,
    val isAnonymous: Boolean
)