package com.ktcompose.composechatapp.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ktcompose.composechatapp.extensions.millisToTimeExt
import com.ktcompose.composechatapp.ui.chat.composes.ProfileAvatar
import androidx.navigation.NavController
import com.ktcompose.composechatapp.constants.K
import com.ktcompose.composechatapp.data.repository.LocalStorageRepo
import com.ktcompose.composechatapp.ui.auth.AuthViewModel

@Composable
fun ChatListScreen(
    navController: NavController,
    rootNav: NavController,
    msgViewModel: MessageViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val myDetail = LocalStorageRepo(context).getMyUser()
//    var userRecordModel by remember { mutableStateOf<UserRecordModel?>(null) }
    val userRecordList by msgViewModel.userRecords.collectAsState()

    Column {
        // Header: Title + User Avatar + logout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text = "Chats",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Avatar + Logout
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileAvatar(
                    profileImageUrl = myDetail?.profileUrl,
                    name = myDetail?.displayName ?: "U",
                    modifier = Modifier.size(40.dp)
                )

                IconButton(onClick = {
                    // logout click code
                    authViewModel.logout()
                    rootNav.navigate(K.LOGIN_SCREEN)
                    {
                        popUpTo(K.HOME_SCREEN) { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Divider line for separation
        Divider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )


        // Show chat list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
        ) {
//            println("General log: list is $userRecordList")
            items(userRecordList) { user ->
                ChatItem(
                    profileImageUrl = user.profileUrl,
                    name = user.displayName!!,
                    lastMessage = user.lastMessage!!,
                    time = user.timestamp.millisToTimeExt(),
                    unreadCount = user.unreadCount,
//                    messageTypeIcon = user.messageTypeIcon
                ) {
                    // Navigate to MessageScreen with user ID
                    navController.navigate("${K.MESSAGE_SCREEN}/${user.uid}")
                }
            }

        }

    }

}





