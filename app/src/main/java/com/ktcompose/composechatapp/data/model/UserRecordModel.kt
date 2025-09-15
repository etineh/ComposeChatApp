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



// Simple helper to convert hex string to Compose Color in the UI layer
fun String.toComposeColor(): Color = Color(android.graphics.Color.parseColor(this))

// Sample data generator
fun sampleChats(): List<UserRecordModel> {
    val now = System.currentTimeMillis()
    return listOf(
        UserRecordModel(
            uid = "1",
            displayName = "George Alan",
            profileUrl = null,
            lastMessage = "Lorem ipsum dolor sit amet consectetur.",
            lastMessageType = MessageType.TEXT,
            timestamp = now - 1000L * 60 * 60 * 3,
            unreadCount = 0,
            userIsOnline = true,
            messageStatus = MessageStatus.READ,
            avatarColorHex = "#2E7D32"
        ),
        UserRecordModel(
            uid = "2",
            displayName = "Uber Cars",
            profileUrl = null,
            lastMessage = "Sender: Lorem ipsum dolor sit amet...",
            lastMessageType = MessageType.TEXT,
            timestamp = now - 1000L * 60 * 60 * 4,
            unreadCount = 0,
            userIsMuted = true,
            messageStatus = MessageStatus.DELIVERED,
            avatarColorHex = "#000000"
        ),
        UserRecordModel(
            uid = "3",
            displayName = "Safiya Fareena",
            profileUrl = null,
            lastMessage = "Video",
            lastMessageType = MessageType.VIDEO,
            timestamp = now - 1000L * 60 * 45,
            unreadCount = 3,
            userIsOnline = false,
            messageStatus = MessageStatus.DELIVERED,
            avatarColorHex = "#7B1FA2"
        ),
        UserRecordModel(
            uid = "4",
            displayName = "Robert Allen",
            profileUrl = null,
            lastMessage = "Photo Lorem ipsum dolor sit amet...",
            lastMessageType = MessageType.PHOTO,
            timestamp = now - 1000L * 60 * 90,
            unreadCount = 0,
            messageStatus = MessageStatus.DELIVERED,
            avatarColorHex = "#1565C0"
        ),
        UserRecordModel(
            uid = "5",
            displayName = "Epic Game",
            profileUrl = null,
            lastMessage = "John Paul: @Robert Lorem ipsum...",
            lastMessageType = MessageType.TEXT,
            timestamp = now - 1000L * 60 * 20,
            unreadCount = 24,
            messageStatus = MessageStatus.DELIVERED,
            avatarColorHex = "#F57C00"
        ),
        UserRecordModel(
            uid = "6",
            displayName = "Scott Franklin",
            profileUrl = null,
            lastMessage = "Audio",
            lastMessageType = MessageType.AUDIO,
            timestamp = now - 1000L * 60 * 120,
            unreadCount = 0,
            messageStatus = MessageStatus.NONE,
            avatarColorHex = "#6A1B9A"
        ),
        UserRecordModel(
            uid = "7",
            displayName = "Muhammed",
            profileUrl = null,
            lastMessage = "😍 Emoji",
            lastMessageType = MessageType.EMOJI,
            timestamp = now - 1000L * 60 * 10,
            unreadCount = 0,
            messageStatus = MessageStatus.READ,
            avatarColorHex = "#00897B"
        ),
        UserRecordModel(
            uid = "8",
            displayName = "Innovative Online Shopping",
            profileUrl = null,
            lastMessage = "Thread Lorem ipsum dolor sit amet...",
            lastMessageType = MessageType.THREAD,
            timestamp = now - 1000L * 60 * 240,
            unreadCount = 2,
            userIsMuted = false,
            messageStatus = MessageStatus.DELIVERED,
            avatarColorHex = "#4E342E"
        )
    )
}
