package com.example.bt_password

import android.app.Application
import com.example.bt_password.util.ThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Main).launch {
            ThemeManager.applySavedTheme(applicationContext)
        }
    }
}
