package com.example.letteblack.screens.settings.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

val Context.settingsDataStore by preferencesDataStore(name = "user_settings")

class SettingDataStore (private val context: Context){

    companion object{
        val NOTIFICATION_KEY = booleanPreferencesKey("notification_enabled")
        val SOUND_KEY = booleanPreferencesKey("sound_enabled")
    }
    suspend fun savedNotificationPreference(enabled: Boolean){
        context.settingsDataStore.edit { prefs ->
            prefs[NOTIFICATION_KEY] = enabled
        }
    }

    suspend fun savedSoundPrefrence(enabled: Boolean){
        context.settingsDataStore.edit { prefs->
            prefs[SOUND_KEY] = enabled
        }
    }

    // Read preferecne
    val notificationPreference: Flow<Boolean> = context.settingsDataStore.data
        .catch { exceptions->
            if(exceptions is IOException){
                emit(androidx.datastore.preferences.core.emptyPreferences())
            }else{
                throw exceptions
            }
        }
        .map { prefs-> prefs[NOTIFICATION_KEY]?: true }

    val soundPrefrences: Flow<Boolean> = context.settingsDataStore.data
        .catch { exceprions->
            if(exceprions is IOException){
                emit(androidx.datastore.preferences.core.emptyPreferences())
            }else{
             throw exceprions
            }
        }
        .map { prefs-> prefs[SOUND_KEY]?: true }
}