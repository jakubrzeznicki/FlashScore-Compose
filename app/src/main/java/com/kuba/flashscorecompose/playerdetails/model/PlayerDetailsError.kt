package com.kuba.flashscorecompose.playerdetails.model

/**
 * Created by jrzeznicki on 01/02/2023.
 */
sealed class PlayerDetailsError {
    object NoError : PlayerDetailsError()
    object EmptyPlayer : PlayerDetailsError()
}