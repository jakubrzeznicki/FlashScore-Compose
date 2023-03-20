package com.kuba.flashscorecompose.data.user.mapper

import com.kuba.flashscorecompose.data.user.local.model.UserEntity
import com.kuba.flashscorecompose.data.user.model.User

/**
 * Created by jrzeznicki on 08/02/2023.
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
