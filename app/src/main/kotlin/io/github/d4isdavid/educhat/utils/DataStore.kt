package io.github.d4isdavid.educhat.utils

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.d4isdavid.educhat.api.enums.NotificationType

val Context.dataStore by preferencesDataStore(name = "app_data")

val CREDENTIALS_EMAIL = stringPreferencesKey("credentials_email")
val CREDENTIALS_PASSWORD = stringPreferencesKey("credentials_password")

val SETTINGS_THEME_MODE = intPreferencesKey("settings_theme_mode")
val SETTINGS_NOTIFICATIONS_IGNORED = stringSetPreferencesKey("settings_notifications_ignored")

enum class ThemeMode(val num: Int) {
    SystemDefault(0),
    Light(1),
    Dark(2);

    companion object {
        fun from(num: Int) = entries.first { it.num == num }
    }
}

fun notificationTypeToStringSet(list: Set<NotificationType>): Set<String> {
    return list.mapTo(mutableSetOf()) { it.num.toString() }
}

fun stringSetToNotificationType(set: Set<String>): Set<NotificationType> {
    return set.mapTo(mutableSetOf()) { NotificationType.from(it.toInt()) }
}
