package com.example.bt_password.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class ThemePreferences(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("app_theme")
        const val LIGHT = "light"
        const val DARK = "dark"
        const val SYSTEM = "system"
    }

    val getTheme: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[THEME_KEY] ?: SYSTEM }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme
        }
    }
}
