package io.github.d4isdavid.educhat.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.credentials: DataStore<Preferences> by preferencesDataStore(name = "credentials")
val CREDENTIALS_EMAIL = stringPreferencesKey("email")
val CREDENTIALS_PASSWORD = stringPreferencesKey("password")
