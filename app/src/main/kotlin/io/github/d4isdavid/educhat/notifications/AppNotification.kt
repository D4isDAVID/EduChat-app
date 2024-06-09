package io.github.d4isdavid.educhat.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.github.d4isdavid.educhat.MainActivity
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.enums.NotificationType
import io.github.d4isdavid.educhat.api.objects.NotificationObject
import io.github.d4isdavid.educhat.api.objects.UserObject

const val APP_NOTIFICATION_CHANNEL_ID = "io.github.d4isdavid.educhat.APP_CHANNEL"

fun createAppNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        APP_NOTIFICATION_CHANNEL_ID,
        context.getString(R.string.app_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    )

    val manager = context.getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(channel)
}

fun postAppNotification(context: Context, n: NotificationObject, u: UserObject) {
    val intent = Intent(context, MainActivity::class.java)
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    val notification = Notification.Builder(context, ERROR_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(
            when (n.type) {
                NotificationType.NewPostReply -> R.drawable.comment
                NotificationType.NewMessageVote -> R.drawable.thumbs_up_down

                NotificationType.HelperGranted,
                NotificationType.HelperRevoked -> R.drawable.star

                NotificationType.AdminGranted,
                NotificationType.AdminRevoked -> R.drawable.admin
            }
        )
        .setContentTitle(n.type.getMessage(context))
        .setContentText(n.type.getDescription(context, u.name))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    val manager = context.getSystemService(NotificationManager::class.java)
    manager.notify(n.id, notification)
}
