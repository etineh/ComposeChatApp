package com.ktcompose.composechatapp.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ktcompose.composechatapp.constants.Colors
import com.ktcompose.composechatapp.constants.K
import com.ktcompose.composechatapp.constants.MessageStatus
import com.ktcompose.composechatapp.data.model.MessageModel
import com.ktcompose.composechatapp.data.model.UserRecordModel
import com.ktcompose.composechatapp.extensions.isFromMeExt
import com.ktcompose.composechatapp.extensions.millisToTimeExt
import com.ktcompose.composechatapp.extensions.toastShort
import com.ktcompose.composechatapp.ui.chat.composes.MessageInputBar
import com.ktcompose.composechatapp.ui.chat.composes.ProfileAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    userRecordModel: UserRecordModel,
    navController: NavHostController,
    messageViewModel: MessageViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }

    val sendState by messageViewModel.sendMessageState.collectAsState()

    LaunchedEffect(userRecordModel.uid) {
        messageViewModel.startObservingMessages(userRecordModel.uid!!)
    }

    val messages by messageViewModel
        .observerMessages[userRecordModel.uid!!]  // get the StateFlow
        ?.collectAsState(initial = emptyList())   // collect safely
        ?: remember { mutableStateOf(emptyList()) } // fallback if not initialised

    val sortedMessages = messages.sortedByDescending { it.timeSent }

    LaunchedEffect(sendState) {
        sendState?.onSuccess {
            println("General log: Message sent successfully")
            messageViewModel.clearSendState()
        }?.onFailure {
            println("General log: Error sending message: ${it.message}")
            messageViewModel.clearSendState()
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scrollState.animateScrollToItem(messages.lastIndex)
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() } ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile image
                    ProfileAvatar(
                        profileImageUrl = userRecordModel.profileUrl,
                        name = userRecordModel.displayName!!,
                        modifier = Modifier.size(40.dp)  // Smaller for header
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = userRecordModel.displayName,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            if (userRecordModel.userIsOnline) K.ONLINE else K.OFFLINE ,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            },
            actions = {
//                IconButton(onClick = { /* video call */ }) {
//                    Icon(Icons.Default., contentDescription = "Video call")
//                }
                IconButton(onClick = { context.toastShort("Call in progress") }) {
                    Icon(Icons.Default.Call, contentDescription = "Voice call")
                }
                IconButton(onClick = { context.toastShort("In progress") }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            },

        )

        // Divider line
        Divider(
            color = Colors.defaultBlackWhite(),
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Messages list
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true, // last item is at bottom
            contentPadding = PaddingValues(12.dp)
        ) {
            items(
                items = sortedMessages,
                key = { message -> message.chatId.orEmpty() }
            ) { message ->
                ChatBubble(message)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        // Send message input
        MessageInputBar(
            inputText = inputText,
            onValueChange = { inputText = it },
            onSend = { message ->
                userRecordModel.uid?.let { uid ->
                    messageViewModel.sendMessage(receiverUid = uid, text = message)
                }
            }
        )
    }
}


@Composable
fun ChatBubble(message: MessageModel) {
    val backgroundColor = if (message.isFromMeExt()) Color(0xFF6750A4) else Color(0xFFE0E0E0)
    val textColor = if (message.isFromMeExt()) Color.White else Color.Black

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromMeExt()) Alignment.End else Alignment.Start
    ) {
        // message
        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(message.text ?: "", color = textColor)
            when (message.messageStatus) {
                MessageStatus.NONE.name -> Text("⏳", fontSize = 10.sp)
                MessageStatus.FAILED.name -> Text("❌ Failed", fontSize = 10.sp, color = Color.Red)
                else -> {} // delivered/normal
            }
        }
        // time
        Text(
            message.timeSent.millisToTimeExt(),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 2.dp, start = 6.dp, end = 6.dp)
        )
    }
}

