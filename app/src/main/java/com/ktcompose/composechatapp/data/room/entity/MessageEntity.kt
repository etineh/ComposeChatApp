package com.ktcompose.composechatapp.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ktcompose.composechatapp.constants.MessageStatus
import com.ktcompose.composechatapp.constants.MessageType

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val chatId: String,        // Firebase id or temp_id
    val conversationId: String,            // sender+receiver key
    val text: String?,
    val senderUid: String,
    val senderDisplayName: String?,
    val senderUsername: String?,
    val timeSent: Long,
    val messageStatus: String
)
