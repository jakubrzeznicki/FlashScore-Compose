package com.kuba.flashscorecompose.data

/**
 * Created by jrzeznicki on 9/9/2022
 */
interface RoomStorage {
    fun getDatabase(): FlashScoreDatabase
}