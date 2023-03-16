package com.example.data.user.mapper

import com.example.database.user.model.UserEntity
import com.example.model.user.User

/**
 * Created by jrzeznicki on 14/03/2023.
 */
fun User.toUserEntity(): UserEntity {
    return UserEntity(
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