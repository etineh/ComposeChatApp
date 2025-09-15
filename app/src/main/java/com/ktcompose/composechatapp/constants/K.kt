package com.ktcompose.composechatapp.constants

import com.google.firebase.auth.FirebaseAuth

object K {

    fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

    const val HOME_GRAPH = "home_graph"
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTER_SCREEN = "register_screen"
    const val HOME_SCREEN = "home_screen"
    const val MESSAGE_SCREEN = "message_screen"


    const val USERS = "Users"
    const val USERS_RECORD = "UserRecords"
    const val MESSAGES = "Messages"

    const val ONLINE = "Online"
    const val OFFLINE = "Offline"
    const val CHAT_ID = "chatId"

//    const val OFFLINE = "Offline"
//    const val OFFLINE = "Offline"


}