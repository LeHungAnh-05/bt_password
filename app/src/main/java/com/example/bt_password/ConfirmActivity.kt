package com.example.bt_password

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)


        val tvInfo = findViewById<TextView>(R.id.tvInfo)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtCode = findViewById<EditText>(R.id.edtCode)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        val email = intent.getStringExtra("email") ?: "No email"
        val code = intent.getStringExtra("code") ?: "No code"
        val password = intent.getStringExtra("password") ?: "No password"

        tvInfo.text = "Xác nhận thông tin"
        edtEmail.setText(email)
        edtCode.setText(code)
        edtPassword.setText(password)

        btnSubmit.setOnClickListener {
            tvInfo.text = "Đã xác nhận thành công!"
        }
    }
}
