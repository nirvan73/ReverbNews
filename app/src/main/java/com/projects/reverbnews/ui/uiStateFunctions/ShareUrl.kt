package com.projects.reverbnews.ui.uiStateFunctions

import android.content.Context
import android.content.Intent

fun ShareUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
        putExtra(Intent.EXTRA_SUBJECT, "Check this out!")
    }
    val chooser = Intent.createChooser(intent, "Share link via")
    context.startActivity(chooser)

}
