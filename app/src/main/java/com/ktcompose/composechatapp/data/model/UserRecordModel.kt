package com.ktcompose.composechatapp.data.model

import androidx.compose.ui.graphics.Color
import com.ktcompose.composechatapp.constants.MessageStatus
import com.ktcompose.composechatapp.constants.MessageType

data class UserRecordModel(
    val uid: String? = null,
    val username: String? = null,
    val displayName: String? = null,
    val profileUrl: String? = null,
    val isGroup: Boolean = false,
    val lastMessage: String? = null,
    val lastMessageType: MessageType = MessageType.TEXT,
    val timestamp: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val userIsMuted: Boolean = false,
    val userIsOnline: Boolean = false,
    val avatarColorHex: String? = null,
    val messageStatus: MessageStatus = MessageStatus.NONE,
//    val messageTypeIcon: ImageVector? = null //
)

