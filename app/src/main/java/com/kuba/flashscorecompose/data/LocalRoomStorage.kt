package com.kuba.flashscorecompose.data

import android.content.Context
import androidx.room.Room

/**
 * Created by jrzeznicki on 9/9/2022
 */
class LocalRoomStorage(private val context: Context) : RoomStorage {
    private val databaseName = DATABASE_NAME
    private val room = Room.databaseBuilder(context, FlashScoreDatabase::class.java, databaseName)
        .fallbackToDestructiveMigration()
        .build()


    override fun getDatabase(): FlashScoreDatabase = room


    private companion object {
        const val DATABASE_NAME = "flashscore_database.db"
    }
}