package com.kuba.flashscorecompose.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by jrzeznicki on 9/5/2022
 */
abstract class FlashScoreDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "flash_score.db"
        private var INSTANCE: FlashScoreDatabase? = null

        fun getInstance(context: Context): FlashScoreDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FlashScoreDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}