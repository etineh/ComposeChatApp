package com.ktcompose.composechatapp.ui.chat.composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ktcompose.composechatapp.extensions.isNullExt

@Composable
fun ProfileAvatar(
    profileImageUrl: String?,
    name: String,
    modifier: Modifier = Modifier
) {
    if (!profileImageUrl.isNullExt()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profileImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Profile image for $name",
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.outlineVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(2).uppercase(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
    }
}