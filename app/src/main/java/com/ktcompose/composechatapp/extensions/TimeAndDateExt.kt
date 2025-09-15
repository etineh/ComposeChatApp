package com.ktcompose.composechatapp.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.millisToTimeExt(): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(this))
}