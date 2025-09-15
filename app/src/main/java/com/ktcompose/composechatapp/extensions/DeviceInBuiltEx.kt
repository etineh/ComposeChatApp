package com.ktcompose.composechatapp.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import android.os.Handler
import android.os.Looper
import com.ktcompose.composechatapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


fun onBackPress(action: () -> Unit) : OnBackPressedCallback {
    return object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            action()
        }
    }
}

fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


fun TextView.copyTextInTVExt() {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText("label", this.text)

    if (clipboard != null && clip != null) {
        clipboard.setPrimaryClip(clip)
    }
}

fun String.copyTextExt(context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText("label", this)
    clipboard?.setPrimaryClip(clip)
}


private var closeApp = false

fun Context.closeApp(): Boolean {
    return if (closeApp) {
        true // Exit the app
    } else {
        closeApp = true
        toastShort("Press again to exit")
        setDelay (5000) { closeApp = false }
        false
    }
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
