package com.example.bt_password.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "theme_prefs")

object ThemeManager {

    private val THEME_KEY = booleanPreferencesKey("is_dark_mode")
    private var currentMode: String = "light"

    fun init(context: Context) {
        // Có thể khởi tạo ở đây nếu cần
    }

    suspend fun getSavedTheme(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[THEME_KEY] ?: false
    }

    suspend fun applySavedTheme(context: Context) {
        val isDark = getSavedTheme(context)
        setTheme(isDark)
    }

    suspend fun saveTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = isDark
        }
        setTheme(isDark)
    }

    fun setTheme(isDark: Boolean) {
        currentMode = if (isDark) "dark" else "light"
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun getCurrentMode() = currentMode
}
