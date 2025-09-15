package com.ktcompose.composechatapp.ui.call

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ktcompose.composechatapp.constants.Colors

@Composable
fun CallsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Call Screen in progress", color = Colors.blackWhite())
        println("General log: I reached")
    }
}
