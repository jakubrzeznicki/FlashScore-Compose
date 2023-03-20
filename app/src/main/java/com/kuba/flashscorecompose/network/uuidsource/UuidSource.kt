package com.kuba.flashscorecompose.network.uuidsource

/**
 * Created by jrzeznicki on 9/5/2022
 */
interface UuidSource {
    fun getRandomUuid(): String
}
