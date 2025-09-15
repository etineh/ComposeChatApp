package com.ktcompose.composechatapp.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ktcompose.composechatapp.ui.chat.composes.ChatHeader
import com.ktcompose.composechatapp.ui.chat.composes.ChatMessageRow
import com.ktcompose.composechatapp.ui.chat.composes.ProfileAvatar

@Composable
fun ChatItem(
    profileImageUrl: String?,
    name: String,
    lastMessage: String,
    time: String,
    unreadCount: Int = 0,
    messageTypeIcon: ImageVector? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 5.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top // Align top for consistent stacking
    ) {
        ProfileAvatar( profileImageUrl, name )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            ChatHeader(  name, time )
            ChatMessageRow( lastMessage, unreadCount, messageTypeIcon)
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ChatItemPreview() {
    MaterialTheme {
        ChatItem(
            profileImageUrl = null,
            name = "Epic Game",
            lastMessage = "John Paul: @Robert Lorem ipsum dolor sit amet...",
            time = "4:30 PM",
            unreadCount = 24
        ) {
            println("I am click")
        }
    }
}
