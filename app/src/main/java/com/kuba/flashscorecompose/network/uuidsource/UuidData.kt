package com.kuba.flashscorecompose.network.uuidsource

import java.util.*

/**
 * Created by jrzeznicki on 9/5/2022
 */
class UuidData : UuidSource {
    override fun getRandomUuid(): String {
        return UUID.randomUUID().toString()
    }
}
