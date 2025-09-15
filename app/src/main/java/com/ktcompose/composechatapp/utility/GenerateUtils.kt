package com.ktcompose.composechatapp.utility

object GenerateUtils {

    // Build conversationId (same for sender & receiver)
    fun getConversationId(senderUid: String, receiverUid: String): String {
        return if (senderUid < receiverUid) {
            "${senderUid}_$receiverUid"
        } else {
            "${receiverUid}_$senderUid"
        }
    }

}