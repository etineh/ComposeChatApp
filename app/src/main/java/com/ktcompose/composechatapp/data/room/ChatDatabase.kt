package com.ktcompose.composechatapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ktcompose.composechatapp.data.room.dao.MessageDao
import com.ktcompose.composechatapp.data.room.dao.UserRecordDao
import com.ktcompose.composechatapp.data.room.entity.MessageEntity
import com.ktcompose.composechatapp.data.room.entity.UserRecordEntity

@Database(
    entities = [MessageEntity::class, UserRecordEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun userRecordDao(): UserRecordDao
}
