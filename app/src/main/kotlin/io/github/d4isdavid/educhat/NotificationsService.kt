package io.github.d4isdavid.educhat

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.params.NotificationsFetchParams
import io.github.d4isdavid.educhat.http.rest.RestClient
import io.github.d4isdavid.educhat.notifications.postAppNotification
import io.github.d4isdavid.educhat.utils.CREDENTIALS_EMAIL
import io.github.d4isdavid.educhat.utils.CREDENTIALS_PASSWORD
import io.github.d4isdavid.educhat.utils.credentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

const val LISTENER_THREAD_NAME = "io.github.d4isdavid.educhat.NotificationsServiceListenerThread"
const val SLEEP_TIME = 5000L

class NotificationsService : Service() {

    private lateinit var api: APIClient
    private lateinit var listenerThread: Thread

    override fun onCreate() {
        val rest = RestClient(BuildConfig.API_BASE_URL, CoroutineScope(Job()))
        api = APIClient(rest)

        listenerThread = thread(start = false, name = LISTENER_THREAD_NAME) {
            try {
                while (true) {
                    Thread.sleep(SLEEP_TIME)
                    try {
                        api.notifications.get(
                            NotificationsFetchParams(onlyNew = true, onlyUnread = true)
                        )
                            .onSuccess {
                                it.forEach { n ->
                                    postAppNotification(
                                        this,
                                        n,
                                        api.users.cache.get(n.userId!!)!!,
                                    )
                                }
                            }
                    } catch (_: Throwable) {
                        // ignored
                    }
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        thread {
            runBlocking {
                credentials.data.map {
                    Pair(it[CREDENTIALS_EMAIL], it[CREDENTIALS_PASSWORD])
                }.collect { (email, password) ->
                    if (email == null || password == null) {
                        return@collect
                    }

                    api.users.logIn(email, password)
                        .onSuccess {
                            if (!listenerThread.isAlive) {
                                listenerThread.start()
                            }
                        }
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        if (!listenerThread.isInterrupted) {
            listenerThread.interrupt()
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}