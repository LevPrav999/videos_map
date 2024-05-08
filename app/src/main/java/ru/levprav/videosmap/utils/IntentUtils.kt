package ru.levprav.videosmap.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

object IntentUtils {
    fun Context.share(
        type: String = "text/plain",
        text: String = ""
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            setType(type)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooserIntent = Intent.createChooser(intent, null)
        startActivity(chooserIntent)
    }

    fun Context.redirectToApp(link: String, type: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        try {
            when (type) {
                "type_youtube" -> intent.setPackage("com.google.android.youtube")
                "type_instagram" -> intent.setPackage("com.instagram.android")
            }
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("share", "redirect fail: ${e.message}")
        }
    }
}