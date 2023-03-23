package com.example.database.user.mapper

import com.example.database.user.model.UserEntity
import com.example.model.user.User

/**
 * Created by jrzeznicki on 13/03/2023.
 */
fun UserEntity.toUser(): User {
    return User(
        id = id,
        name = name,
        email = email,
        password = password,
        phone = phone,
        address = address,
        photoUri = photoUri,
        isAnonymous = isAnonymous
    )
}