package io.github.d4isdavid.educhat.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import io.github.d4isdavid.educhat.ErrorActivity
import io.github.d4isdavid.educhat.R

const val ERROR_NOTIFICATION_CHANNEL_ID = "io.github.d4isdavid.educhat.ERRORS_CHANNEL"
const val ERROR_EXCEPTION_MESSAGE_EXTRA = "io.github.d4isdavid.educhat.ExceptionMessage"
const val ERROR_EXCEPTION_STACK_EXTRA = "io.github.d4isdavid.educhat.ExceptionStack"

fun createErrorNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        ERROR_NOTIFICATION_CHANNEL_ID,
        context.getString(R.string.error_notification_channel_name),
        NotificationManager.IMPORTANCE_LOW,
    )

    val manager = context.getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(channel)
}

fun postErrorNotification(context: Context, t: Thread, e: Throwable) {
    val intent = Intent(context, ErrorActivity::class.java)
    intent.putExtra(ERROR_EXCEPTION_MESSAGE_EXTRA, e.localizedMessage ?: "None")
    intent.putExtra(ERROR_EXCEPTION_STACK_EXTRA, e.stackTraceToString().trim())

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    val notification = Notification.Builder(context, ERROR_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.bug_report)
        .setContentTitle("Unhandled Exception")
        .setContentText(e::class.simpleName ?: "Unknown error")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    val manager = context.getSystemService(NotificationManager::class.java)
    manager.notify((t.id + e.hashCode()).toInt(), notification)

    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context, R.string.error_toast, Toast.LENGTH_LONG).show()
    }
}
