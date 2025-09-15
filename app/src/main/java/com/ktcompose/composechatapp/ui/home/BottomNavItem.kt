package com.ktcompose.composechatapp.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    object Chats : BottomNavItem("Chats", Icons.Default.Chat, "chats")
    object Calls : BottomNavItem("Calls", Icons.Default.Call, "calls")
    object Users : BottomNavItem("Users", Icons.Default.Person, "users")
    object Groups : BottomNavItem("Groups", Icons.Default.Group, "groups")
}
