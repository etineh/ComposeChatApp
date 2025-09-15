package com.ktcompose.composechatapp.ui.group

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ktcompose.composechatapp.constants.Colors

@Composable
fun GroupsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Groups screen in progress", color = Colors.blackWhite())
    }
}