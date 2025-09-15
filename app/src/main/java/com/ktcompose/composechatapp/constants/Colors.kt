package com.ktcompose.composechatapp.constants

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

object Colors {
    @Composable
    fun blackWhite() = MaterialTheme.colorScheme.primary

    @Composable
    fun defaultBlackWhite() = MaterialTheme.colorScheme.onPrimary

    @Composable
    fun background() = MaterialTheme.colorScheme.background

    @Composable
    fun onBackground() = MaterialTheme.colorScheme.onBackground

}