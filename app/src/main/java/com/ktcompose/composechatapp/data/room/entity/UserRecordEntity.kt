package com.ktcompose.composechatapp.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_records")
data class UserRecordEntity(
    @PrimaryKey val uid: String,
    val displayName: String?,
    val username: String?,
    val profileUrl: String?,
    val lastMessage: String?,
    val timestamp: Long
)

