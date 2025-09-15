package com.ktcompose.composechatapp.ui.chat.composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ktcompose.composechatapp.constants.Colors
import com.ktcompose.composechatapp.extensions.toastShort

@Composable
fun MessageInputBar(
    inputText: String,
    onValueChange: (String) -> Unit,
    onSend: (String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(
                MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp) // for inner padding
    ) {
        // Text Input
        OutlinedTextField(
            value = inputText,
            onValueChange = onValueChange,
            placeholder = { Text("Type your message...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            shape = RoundedCornerShape(24.dp),
            maxLines = 3,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Divider line
        Divider(
            color = Colors.defaultBlackWhite(),
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Icon row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                CompositionLocalProvider(LocalContentColor provides Colors.defaultBlackWhite()) {
                    IconButton(onClick = { context.toastShort("In progress") }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Attach")
                    }
                    IconButton(onClick = { context.toastShort("In progress") }) {
                        Icon(imageVector = Icons.Filled.Mic, contentDescription = "Voice")
                    }
                    IconButton(onClick = { context.toastShort("In progress") }) {
                        Icon(imageVector = Icons.Filled.SentimentSatisfied, contentDescription = "Emoji")
                    }
                    IconButton(onClick = { context.toastShort("In progress") }) {
                        Icon(imageVector = Icons.Filled.AttachFile, contentDescription = "Clip")
                    }
                    IconButton(onClick = { context.toastShort("In progress") }) {
                        Icon(imageVector = Icons.Outlined.Star, contentDescription = "Star")
                    }
                }

            }

            // Send button
            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onSend(inputText)
                        onValueChange("")
                    }
                },
                enabled = inputText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = if (inputText.isNotBlank()) Color.Blue else Color.Gray
                )
            }
        }
    }
}
