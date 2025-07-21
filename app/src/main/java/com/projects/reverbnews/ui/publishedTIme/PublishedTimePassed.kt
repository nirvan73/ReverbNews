package com.projects.reverbnews.ui.publishedTIme

import android.os.Build
import java.time.Duration
import java.time.Instant

fun getTimeAgo(publishedAt: String): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val publishedTime = Instant.parse(publishedAt)
            val now = Instant.now()
            val diff = Duration.between(publishedTime, now)

            when {
                diff.toMinutes() < 1 -> "Just now"
                diff.toMinutes() < 60 -> "${diff.toMinutes()} min ago"
                diff.toHours() < 24 -> "${diff.toHours()} hr ago"
                diff.toDays() == 1L -> "Yesterday"
                diff.toDays() < 7 -> "${diff.toDays()} days ago"
                else -> "${diff.toDays() / 7} weeks ago"
            }
        } else {
            "Some time ago"
        }
    } catch (e: Exception) {
        "Unknown time"
    }
}
