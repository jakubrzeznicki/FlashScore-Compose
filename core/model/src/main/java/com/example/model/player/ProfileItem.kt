package com.example.model.player

/**
 * Created by jrzeznicki on 08/02/2023.
 */
sealed class ProfileItem(open val value: String) {
    class Name(override val value: String) : ProfileItem(value)
    class Email(override val value: String) : ProfileItem(value)
    class Phone(override val value: String) : ProfileItem(value)
    class Address(override val value: String) : ProfileItem(value)
}
