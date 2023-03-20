package com.kuba.flashscorecompose.data.userpreferences.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscorecompose.data.userpreferences.local.model.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/02/2023.
 */
@Dao
interface UserPreferencesDao {

    @Query("SELECT is_on_boarding_completed FROM user_preferences WHERE user_id = :id")
    suspend fun getIsOnBoardingCompleted(id: String): Boolean?

    @Query("SELECT * FROM user_preferences WHERE user_id = :id")
    suspend fun getUserPreferences(id: String): UserPreferencesEntity?

    @Query("SELECT * FROM user_preferences WHERE user_id = :id")
    fun observeUserPreferences(id: String): Flow<UserPreferencesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserPreferences(userPreferencesEntity: UserPreferencesEntity)

    @Query("DELETE FROM user_preferences WHERE user_id = :userId")
    fun deleteUserPreferences(userId: String)
}