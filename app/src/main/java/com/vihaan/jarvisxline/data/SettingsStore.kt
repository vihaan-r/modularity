package com.vihaan.jarvisxline.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vihaan.jarvisxline.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

private val Context.dataStore by preferencesDataStore(name = "jarvis_settings")

class SettingsStore(private val context: Context) {
    private val hfTokenKey = stringPreferencesKey("hf_token")
    private val appApiKey = stringPreferencesKey("app_api_key")
    private val navigatorPathKey = stringPreferencesKey("navigator_path")

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        val existingKey = prefs[appApiKey] ?: "jarvis-${UUID.randomUUID()}"
        AppSettings(
            huggingFaceToken = prefs[hfTokenKey] ?: "",
            appApiKey = existingKey,
            navigatorPath = prefs[navigatorPathKey] ?: "navigator_weights.pt"
        )
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[hfTokenKey] = token }
    }

    suspend fun saveNavigatorPath(path: String) {
        context.dataStore.edit { it[navigatorPathKey] = path }
    }

    suspend fun regenerateApiKey() {
        context.dataStore.edit { it[appApiKey] = "jarvis-${UUID.randomUUID()}" }
    }
}
