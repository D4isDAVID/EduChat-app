package io.github.d4isdavid.educhat

import android.app.Application
import io.github.d4isdavid.educhat.notifications.createErrorNotificationChannel
import io.github.d4isdavid.educhat.notifications.postErrorNotification


class EduChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        createErrorNotificationChannel(this)

        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            postErrorNotification(this, t, e)
            oldHandler?.uncaughtException(t, e)
        }
    }

}
