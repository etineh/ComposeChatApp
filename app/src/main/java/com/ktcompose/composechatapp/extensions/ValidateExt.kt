package com.ktcompose.composechatapp.extensions

import com.ktcompose.composechatapp.constants.K
import com.ktcompose.composechatapp.data.model.MessageModel


fun String.isEmailValidExt(): Boolean {
    return this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun MessageModel.isFromMeExt() : Boolean {
    return this.senderUid == K.getCurrentUserId()
}


