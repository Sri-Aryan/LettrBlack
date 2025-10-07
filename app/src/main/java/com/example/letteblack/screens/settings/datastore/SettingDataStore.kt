package com.example.letteblack.screens.settings.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore by preferencesDataStore(name = "user_settings")

class SettingDataStore (private val context: Context){

    companion object{
        val NOTIFICATION_KEY = booleanPreferencesKey("notification_enabled")
    }
    suspend fun savedNotificationPreference(enabled: Boolean){
        context.settingsDataStore.edit { prefs ->
            prefs[NOTIFICATION_KEY] = enabled
        }
    }

    // Read preferecne
    val notificationPreference: Flow<Boolean> = context.settingsDataStore.data
        .map { prefs-> prefs[NOTIFICATION_KEY]?: true }

}