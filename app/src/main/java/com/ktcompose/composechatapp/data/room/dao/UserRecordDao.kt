package com.ktcompose.composechatapp.data.room.dao

import androidx.room.*
import com.ktcompose.composechatapp.data.room.entity.UserRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRecordDao {

    @Query("SELECT * FROM user_records ORDER BY timestamp DESC")
    fun observeUserRecords(): Flow<List<UserRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserRecordEntity>)

    @Query("DELETE FROM user_records")
    suspend fun clearAll()
}
