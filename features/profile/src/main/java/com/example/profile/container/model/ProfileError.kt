package com.example.profile.container.model

/**
 * Created by jrzeznicki on 24/03/2023.
 */
sealed class ProfileError {
    object NoError: ProfileError()
    object EmptyProfile: ProfileError()
}