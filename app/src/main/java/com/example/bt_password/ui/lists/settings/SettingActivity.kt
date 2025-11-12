package com.example.bt_password.ui.settings

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bt_password.R
import com.example.bt_password.util.ThemeManager
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val btnApply = findViewById<Button>(R.id.btnApply)
        val rbLight = findViewById<RadioButton>(R.id.rbLight)
        val rbDark = findViewById<RadioButton>(R.id.rbDark)

        // Đọc và thiết lập trạng thái ban đầu cho RadioButton
        lifecycleScope.launch {
            val isDark = ThemeManager.getSavedTheme(this@SettingActivity)
            if (isDark) {
                rbDark.isChecked = true
            } else {
                rbLight.isChecked = true
            }
        }

        // Khi nhấn nút Apply
        btnApply.setOnClickListener {
            val isDarkSelected = rbDark.isChecked
            // Chỉ cần gọi saveTheme là đủ. Nó đã bao gồm cả việc áp dụng theme.
            lifecycleScope.launch {
                Log.d("SettingActivity", "Button Apply clicked. Saving theme, isDark: $isDarkSelected")
                ThemeManager.saveTheme(this@SettingActivity, isDarkSelected)
                // ❌ XÓA DÒNG NÀY ĐI
                // ThemeManager.setTheme(isDarkSelected)
            }
        }
    }
}
