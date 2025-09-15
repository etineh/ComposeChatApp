package com.ktcompose.composechatapp.data.model

import com.ktcompose.composechatapp.constants.MessageStatus

data class MessageModel(
    val text: String? = null,
    var chatId: String? = null,
    val senderUid: String? = null,
    val senderDisplayName: String? = null,
    val senderUsername: String? = null,
    val messageStatus: String = MessageStatus.NONE.name,
    val timeSent: Long = System.currentTimeMillis()
)
