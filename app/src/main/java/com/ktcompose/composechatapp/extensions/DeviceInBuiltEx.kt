package com.ktcompose.composechatapp.extensions

import android.content.Context
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun setDelay(duration: Long = 10_000, onComplete : () -> Unit){
    Handler(Looper.getMainLooper()).postDelayed({
        onComplete()
    }, duration)
}

fun Any?.isNullExt(): Boolean {
    return this == null || (this is String && (this.isEmpty() || this == "null" || this == ""))
}

fun checkNetwork(callback: (networkIsOkay: Boolean) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val urlc = URL("https://www.google.com").openConnection() as HttpURLConnection
            urlc.apply {
                setRequestProperty("User-Agent", "Test")
                setRequestProperty("Connection", "close")
                connectTimeout = 2000
                connect()
            }
            val responseCode = urlc.responseCode
            urlc.disconnect() // Clean up connection

            withContext(Dispatchers.Main) {
                callback(responseCode == 200 || responseCode == 204)
            }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                println("Network check failed: ${e.message}")
                callback(false)
            }
        }
    }
}
